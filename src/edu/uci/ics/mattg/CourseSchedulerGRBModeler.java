package edu.uci.ics.mattg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.uci.ics.mattg.collections.ArrayList;
import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.collections.Map;

import gurobi.GRB;
import gurobi.GRBConstr;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBQuadExpr;
import gurobi.GRBVar;

public class CourseSchedulerGRBModeler {
	private GRBModel model;
	private GRBEnv environment;
	private GRBVar var, var2;
	private Connection connection;
	private Statement statement;
	private ArrayList<ArrayList<String>> selSets;
	private ArrayList<Integer> reqAmounts;
	private ArrayList<String> quarters;
	private ArrayList<String> courseOfferings;
	private ArrayList<String> prereqs;
	private Map<String, ArrayList<String>> prereqMap;
	private String major, specialization, startQuarter;
	private int startYear;
	
	private enum Quarter {
		Winter("Winter"),
		Spring("Spring"),
		Fall("Fall");

		public final String text;
		Quarter(String text) {
			this.text = text;
		}
	}

	public CourseSchedulerGRBModeler(String major, String specialization, String startQuarter, String endQuarter, int startYear, int endYear) {
		this.major = major;
		this.specialization = specialization;
		this.startQuarter = startQuarter;
		this.startYear = startYear;

		try {
			selSets = new ArrayList<ArrayList<String>>();
			reqAmounts = new ArrayList<Integer>();
			quarters = new ArrayList<String>();
			courseOfferings = new ArrayList<String>();
			prereqMap = new ArrayMap<String,ArrayList<String>>();

			// Build set of Quarters e.g. Spring2012, Fall2012, Winter2013...
			for(int i = Quarter.valueOf(startQuarter).ordinal(); i <= ((endYear-startYear)*Quarter.values().length)+Quarter.valueOf(endQuarter).ordinal(); i++) {
				System.out.println(i + ": " + Quarter.values()[(i%Quarter.values().length)].text + (startYear+(i/Quarter.values().length)));
				quarters.add(Quarter.values()[(i%Quarter.values().length)].text + (startYear+(i/Quarter.values().length)));
			}

			// Create MySQL Connection
			Class.forName(ProgramState.jdbcDriver);
			connection = DriverManager.getConnection(ProgramState.url);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			FetchSelectionSets();
			FetchCourseOfferings();
			FetchCourseRequirements();

		} catch (Exception ex) {
			ex.printStackTrace(); 
		}
	}

	public void model() {
		try {
			// Initialize environment and model
			environment = new GRBEnv();
			model = new GRBModel(environment);
			model.set(GRB.StringAttr.ModelName, "Course_Scheduler");

			/**************** Add Variables & Constraints ****************/
			GRBLinExpr objExpr = new GRBLinExpr();
			for(CourseItem c : ProgramState.courseMap.values()) {
				for(String q : quarters.elements()) {
					// Add decision variables 'schedule' for required course c in quarter q. Name: planned_<department>_<coursenumber>_<quarter>
					var = model.addVar(0.0, 1.0, 1.0, GRB.BINARY, "planned_" + c.getDepartment()+"_"+c.getNumber() + "_" + q);
					// Add decision variable to objective function expression
					objExpr.addTerm(1.0, var);
				}
			}
			model.update();

			// Constrain scheduled course c in quarter q to be <= offered course c in quarter q. Name: schedoffered_<department>_<coursenumber>_<quarter>
			String offering;
			GRBLinExpr schedOfferedExpr = new GRBLinExpr();
			for(CourseItem c : ProgramState.courseMap.values()) {
				for(String q : quarters.elements()) {
					schedOfferedExpr = new GRBLinExpr();
					offering = c.getDepartment() + "_" + c.getNumber() + "_" + q;
					var = model.getVarByName("planned_" + offering);
					schedOfferedExpr.addTerm(1.0, var);

					if(courseOfferings.contains(offering))
						model.addConstr(schedOfferedExpr, GRB.LESS_EQUAL, 1.0, "schedoffered_" + offering);
					else
						model.addConstr(schedOfferedExpr, GRB.LESS_EQUAL, 0.0, "schedoffered_" + offering);
				}
			}

			// Course c taken and the sum of courses scheduled in quarter q <= 1 for all courses. Name planonce_<department>_<coursenumber>
			GRBLinExpr countOnceExpr;
			for(CourseItem c : ProgramState.courseMap.values()) {
				countOnceExpr = new GRBLinExpr();
				for(String q : quarters.elements()) {
					// Add variables of for scheduled course c in all quarters
					var = model.getVarByName("planned_" + c.getDepartment() + "_"+c.getNumber() + "_" + q);
					countOnceExpr.addTerm(1.0, var);
				}
				if(c.isSelected())
					model.addConstr(countOnceExpr, GRB.LESS_EQUAL, 0.0, "planonce_" +c.getDepartment()+"_"+c.getNumber());
				else
					model.addConstr(countOnceExpr, GRB.LESS_EQUAL, 1.0, "planonce_" +c.getDepartment()+"_"+c.getNumber());
			}

			// Number of units of scheduled courses <= 20 for all quarters. Name: unit_limit_<quarter>
			GRBLinExpr unitLimitExpr;
			for(String q : quarters.elements()) {
				unitLimitExpr = new GRBLinExpr();
				for(CourseItem c : ProgramState.courseMap.values()) {
					unitLimitExpr.addTerm(c.getUnits(), model.getVarByName("planned_" + c.getDepartment()+"_"+c.getNumber() + "_" + q));
				}
				model.addConstr(unitLimitExpr, GRB.LESS_EQUAL, ProgramState.UNIT_LIMIT, "unit_limit_" + ProgramState.UNIT_LIMIT + "_" + q);
			}

			// Require s amount of courses from selection set S(d). Name: selection_set<i>_<amount>_required
			int i = 0;
			int taken;
			GRBLinExpr selSetReqExpr;
			for(ArrayList<String> ss : selSets.elements()) {
				selSetReqExpr = new GRBLinExpr();
				taken = 0; 
				for(String c : ss.elements()) {
					for(CourseItem c2 : ProgramState.courseMap.values())
						if(c2.isSelected())
							if((c2.getDepartment()+"_"+c2.getNumber()).equals(c))
								taken++;
					for(String q : quarters.elements()) {
						var = model.getVarByName("planned_" + c + "_" + q);
						selSetReqExpr.addTerm(1.0, var);
					}
				}
				model.addConstr(selSetReqExpr, GRB.GREATER_EQUAL, reqAmounts.get(i)-taken, "selection_set" + i + "_" + reqAmounts.get(i++) + "_required");
			}

			// For all courses c, if planned in quarter q, all prerequisites must have been completed previously
			String quarter, year;
			double endTerm, term;
			GRBQuadExpr qexpr, qexpr2;
			for(CourseItem c : ProgramState.courseMap.values()) {
				for(String q : quarters.elements()) {
					qexpr = new GRBQuadExpr();
					qexpr2 = new GRBQuadExpr();
					var = model.getVarByName("planned_" + c.getDepartment() + "_" + c.getNumber() + "_" + q);
					prereqs = prereqMap.getValue(c.getDepartment() + "_" + c.getNumber());
					if(prereqs != null) {
						quarter = q.substring(0, q.length()-4);
						year = q.substring(q.length()-4);
						// To compare quarters: year + enumeration/3 (0/3:winter, 1/3:spring, 2/3:fall) and compare the double values
						endTerm = Double.parseDouble(year) + (Quarter.valueOf(quarter).ordinal()/3.00);
						for(String f : prereqs.elements()) {
							term = startYear + (Quarter.valueOf(startQuarter).ordinal()/3.00);
							while(term < endTerm) {
								var2 = model.getVarByName("planned_" + f + "_" + getQuarterEnum((int)((term - (int)term)*3.00)) + (int)term);
								qexpr.addTerm(1.0, var, var2);
								term += 1.00/3.00;
							}
						}
						qexpr2.addTerm(prereqs.size(), var);
						//model.addQConstr(qexpr, GRB.EQUAL, qexpr2, "prereqs_taken_for_" + c.getDepartment() + "_" + c.getNumber());
					}
				}
			}
			model.update();

			/**************** Set Objective ****************/
			model.setObjective(objExpr,GRB.MINIMIZE);

			// Print Variables and Constraints
			GRBVar vars[] = model.getVars();
			System.out.println("\tVariables");
			if(vars != null)
				for(GRBVar var : vars) {
					System.out.println(var.get(GRB.StringAttr.VarName) + ":[" + var.get(GRB.DoubleAttr.LB) + "," + var.get(GRB.DoubleAttr.UB) + "]");
				}

			GRBConstr constrs[] = model.getConstrs();
			System.out.println("\tConstraints");
			if(constrs != null)
				for(GRBConstr constr : constrs) {
					GRBLinExpr lhs = model.getRow(constr);
					System.out.print(constr.get(GRB.StringAttr.ConstrName) + ": ");
					for(int j = 0; j < lhs.size(); j++)
						System.out.print((j==0?"":" + ") + lhs.getCoeff(j) + "*" + lhs.getVar(j).get(GRB.StringAttr.VarName));
					System.out.println(" " + constr.get(GRB.CharAttr.Sense) + " " + constr.get(GRB.DoubleAttr.RHS));
				}

			//			GRBQConstr qConstrs[] = model.getQConstrs();
			//			System.out.println("\tQuadratic Constraints");
			//			if(constrs != null)
			//				for(GRBQConstr qConstr : qConstrs) {
			//					System.out.println(qConstr.get(GRB.StringAttr.QCName) + ": ");
			//					GRBQuadExpr lhs = model.getQCRow(qConstr);
			//					for(int j = 0; j < lhs.size(); j++)
			//						System.out.print((j==0?"":" + ") + lhs.getVar1(j).get(GRB.StringAttr.VarName) + "*" + lhs.getVar2(j).get(GRB.StringAttr.VarName));
			//					System.out.print(qConstr.get(GRB.CharAttr.Sense) + qConstr.get(GRB.DoubleAttr.QCRHS));
			//				}

		} catch (GRBException e1) {
			System.out.println(e1.getErrorCode() + ", " + e1.getMessage());
			//e1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace(); 
		}
	}

	public boolean optimize() {
		ProgramState.courseSchedule = new ArrayList<String>();
		
		try {
			model.optimize();
			System.out.println("\tSolution: ");
			for(GRBVar v : model.getVars()) {
				System.out.println(v.get(GRB.StringAttr.VarName) + " " + v.get(GRB.DoubleAttr.X));
				if(v.get(GRB.DoubleAttr.X) > 0)
					ProgramState.courseSchedule.add(v.get(GRB.StringAttr.VarName));
			}

		} catch (GRBException e1) {
			System.out.println(e1.getErrorCode() + ", " + e1.getMessage());
			//e1.printStackTrace();
			return false;
		}

		return true;
	}

	private void FetchSelectionSets() throws SQLException {
		// Query database to get selection sets and completion requirements
		String query1 = "SELECT selset.selection_set_id, c.department, c.course_number, creq.selection_set_req FROM degree AS d, completion_req AS creq, selection_sets AS selset, " +
				"courses AS c WHERE d.degree_id=creq.degree_id AND creq.selection_set_id=selset.selection_set_id AND c.course_id=selset.course_id AND d.major='";
		String query2 = "' AND d.specialization='";
		String query3 = "' ORDER BY selset.selection_set_id";
		System.out.println(query1+major+query2+specialization+query3);
		ResultSet rs = statement.executeQuery(query1+major+query2+specialization+query3);

		// Build Selection sets
		if(rs.first()) {
			int id = -1;
			int i = -1;
			for(rs.first(); !rs.isAfterLast(); rs.next()) {
				int ssID = rs.getInt(1);
				if(ssID != id) {
					i++;
					id = ssID;
					reqAmounts.add(new Integer(rs.getInt(4)));
					selSets.add(new ArrayList<String>());
				}
				selSets.get(selSets.size()-1).add(rs.getString(2) + "_" + rs.getString(3));
			}

			System.out.println("\tSelection Sets Loaded:");
			for(i = 0; i < selSets.size(); i++) {
				System.out.println("SS" + i + "\nAmount: " + reqAmounts.get(i));
				System.out.println(selSets.get(i));
			}
		}
	}

	private void FetchCourseOfferings() throws SQLException {
		// Query database to get selection sets and completion requirements
		String query = "SELECT quarter, year, department, course_number FROM course_offerings, courses" +
				" WHERE course_offerings.course_id=courses.course_id";

		System.out.println(query);
		ResultSet rs = statement.executeQuery(query);

		// Build Selection sets
		if(rs.first()) {
			for(rs.first(); !rs.isAfterLast(); rs.next())
				courseOfferings.add(rs.getString(3) + "_" + rs.getString(4) + "_" + rs.getString(1) + rs.getString(2).substring(0, 4));

			System.out.println("\tCourse Offerings Loaded:");
			for(String cO : courseOfferings.elements()) {
				System.out.println(cO);
			}
		}
	}

	private void FetchCourseRequirements() throws SQLException {
		// Query database to get all courses with prerequisite courses
		ArrayList<String> prereqs;
		String query = "SELECT c1.department, c1.course_number, group_concat(c2.department,'_',c2.course_number separator ',') FROM courses AS c1, courses AS c2, prereqs as p WHERE p.course_id=c1.course_id AND p.prereq_id=c2.course_id GROUP BY c1.department, c1.course_number";

		System.out.println(query);
		ResultSet rs = statement.executeQuery(query);

		// Build prerequisite map
		if(rs.first()) {
			for(rs.first(); !rs.isAfterLast(); rs.next()) {
				prereqs = new ArrayList<String>();
				String key1 = rs.getString(1) + "_" + rs.getString(2);
				String ps[] = rs.getString(3).split(",");
				for(String p : ps)
					prereqs.add(p);
				//			System.out.println("Adding " + key1 + ": " + prereqs);
				prereqMap.put(key1, prereqs);
			}

			System.out.println("\tPrerequisites Loaded:");
			System.out.println(prereqMap);
		}
	}

	private Quarter getQuarterEnum(int i) {
		switch(i) {
		case 0:
			return Quarter.Winter;
		case 1:
			return Quarter.Spring;
		case 2:
			return Quarter.Fall;
		}
		return null;
	}
}
