package edu.uci.ics.mattg;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.uci.ics.mattg.collections.ArrayList;
import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.ui.DynamicMapDialog;

public class CourseScheduler extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final short BUTTON_PADDING = 10;
	private JButton courseSelect, specializationSelect, generateFormulas, manageDB;
	private GridLayout layout;
	private ButtonGroup degreeGroup;
	public static DynamicMapDialog degreeSelectDialog;
	public static DynamicMapDialog courseSelectDialog;
	public static CourseOfferingsPanel coursesOfferingsDialog;
	public static ScheduleGenDialog scheduleGenDialog;
	public static ManageDatabaseDialog updateDBDialog;

	public CourseScheduler() {
		// Configure LookAndFeel
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Configure Log
		// TODO: Complete info/debug/error logging
		Logger.getLogger("edu.uci.ics.mattg.CourseScheduler").info("Course Scheduler Log");

		// courseDT = new DigitalTree();
		ProgramState.courseMap = new ArrayMap<String,CourseItem>();
		ProgramState.degreeMap = new ArrayMap<String,DegreeItem>(); 
		ProgramState.selectionSetList = new ArrayList<SelectionSetItem>();
		degreeGroup = new ButtonGroup();

		// Build Dialogs
		loadData();

		degreeSelectDialog = new DynamicMapDialog(this, "Pick one or more degrees:", "Degree Selection", ProgramState.degreeMap);

		courseSelectDialog = new DynamicMapDialog(this, "Select all completed courses:", "Course Selection", ProgramState.courseMap);

		scheduleGenDialog = new ScheduleGenDialog(this);

		updateDBDialog = new ManageDatabaseDialog(this);

		// Menu buttons
		courseSelect = new JButton("Select Completed Courses");
		courseSelect.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if(ProgramState.coursesUpdated) {
					courseSelectDialog.updateModel();
				}
				courseSelectDialog.setVisible(true);
				ProgramState.coursesUpdated = false;
			}
		});

		specializationSelect = new JButton("Select Degree(s)");
		specializationSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ProgramState.degreesUpdated) {
					degreeSelectDialog.updateModel();

					// Since two views must have degrees updated in them, require 
					// an intermediate state for one beign updated and the other not.
					if(ProgramState.degreesUpdated2)
						ProgramState.degreesUpdated2 = false;
					else
						ProgramState.degreesUpdated = false;
				}
				degreeSelectDialog.setVisible(true);
			}
		});

		generateFormulas = new JButton("Generate Schedule");
		generateFormulas.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				scheduleGenDialog.setVisible(true);
			}
		});

		manageDB = new JButton("Manage Database");
		manageDB.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				updateDBDialog.setVisible(true);
			}
		});

		layout = new GridLayout(4,1);
		layout.setHgap(BUTTON_PADDING);
		layout.setVgap(BUTTON_PADDING);

		this.setLayout(layout);
		this.add(courseSelect);
		this.add(specializationSelect);
		this.add(generateFormulas);
		this.add(manageDB);
		this.setSize(300,250);
		this.setTitle("Course Planner");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void loadData() {
		// Fetch course and specialization list from MySQL server
		try {
			// Connection setup
			Class.forName(ProgramState.jdbcDriver);
			Connection con = DriverManager.getConnection(ProgramState.url);
			Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// Populate courses
			ResultSet rs = statement.executeQuery("SELECT * FROM courses");
			if (rs.next()){
				rs.first();
				for(CourseItem newItem; !rs.isAfterLast(); rs.next()) {
					String courseDepartment = rs.getString(2);
					String courseNumber = rs.getString(3);
					String courseTitle = rs.getString(4);

					newItem = new CourseItem(courseDepartment, courseNumber, courseTitle, rs.getInt(5), rs.getString(6));

					String key = (courseDepartment + courseNumber + ":" + courseTitle).toLowerCase().replaceAll(" ", "");
					ProgramState.courseMap.put(key, newItem);
				}
			}
			System.out.println(ProgramState.courseMap);

			// Populate degrees
			rs = statement.executeQuery("SELECT * FROM degree");
			if (rs.next()){
				rs.first();
				for(DegreeItem newItem; !rs.isAfterLast(); rs.next()) {
					String specialization = rs.getString(2);
					String major = rs.getString(3);

					newItem = new DegreeItem(major, specialization, rs.getString(4));

					// TODO: Add ':' as well to Digital Tree
					String key = (major + specialization).toLowerCase().replaceAll(" ", "");

					ProgramState.degreeMap.put(key, newItem);
					degreeGroup.add(newItem);
				}
			}
			System.out.println(ProgramState.degreeMap);

			// Populate selection sets
			rs = statement.executeQuery("SELECT selection_set_id, group_concat(courses.department,course_number separator ', ') FROM selection_sets NATURAL JOIN courses GROUP BY selection_set_id");
			//group_concat(courses.department,course_number separator ', ')
			if (rs.next()){
				rs.first();
				for(SelectionSetItem newItem; !rs.isAfterLast(); rs.next()) {
					int id = rs.getInt(1);
					String set = rs.getString(2);
					Logger.getLogger("edu.uci.ics.mattg.CourseScheduler").info("Adding selection set item '" + id + " " + set + "'");

					newItem = new SelectionSetItem(id, set);

					ProgramState.selectionSetList.add(newItem);
				}
			}
			System.out.println(ProgramState.selectionSetList);

			// Close connection
			rs.close();
			statement.close();
			con.close();

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (SQLException ex2) {
			ex2.printStackTrace();
		}
	}
}
