package edu.uci.ics.mattg;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;
import edu.uci.ics.mattg.ui.NakedButton;

public class ManageDatabaseDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String COURSES = "Courses";
	private static final String DEGREES = "Degrees";
	private static final String COMPLETION_REQ = "Degree Requirements";
	private NakedButton coursesTab, degreesTab, degreeRequirements;
	private JButton addCourse, deleteCourse, updateCourse, addDegree, deleteDegree, updateDegree, addDegreeReq, deleteDegreeReq, updateDegreeReq, done;
	private JPanel tablePanel, coursePanel, degreePanel, degreeReqPanel, cButtonPanel, dButtonPanel, dRButtonPanel, donePanel, courseOfferingsPanel;
	private CardLayout cardLayout;
	private QueryTableModel courseQTM, degreeQTM, degreeReqQTM, selectionSetQTM;
	private QueryTable courseQT, degreeQT, degreeReqQT;
	private JScrollPane courseSP, degreeSP, degreeReqSP;
	private AddCourseDialog addCourseDialog;
	private UpdateCourseDialog updateCourseDialog;
	private AddDegreeDialog addDegreeDialog;
	private UpdateDegreeDialog updateDegreeDialog;
	private AddDegreeRequirementDialog addSelSetDialog;
	private UpdateDegreeRequirementDialog updateDegreeReqDialog;
	private JTabbedPane tabPane;

	// TODO: Delete multiple selected tuples
	public ManageDatabaseDialog(JFrame parent) {
		super(parent, "Manage Database");

		// Build table selection tabs
		cardLayout = new CardLayout();
		tablePanel = new JPanel(cardLayout);
		
		coursesTab = new NakedButton(COURSES);
		coursesTab.setSelected(true);
		coursesTab.getModel().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				cardLayout.show(tablePanel, coursesTab.getText());
		    }
		});
		
		degreesTab = new NakedButton(DEGREES);
		degreesTab.getModel().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				cardLayout.show(tablePanel, degreesTab.getText());
		    }
		});
		
		degreeRequirements = new NakedButton(COMPLETION_REQ);
		degreeRequirements.getModel().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				cardLayout.show(tablePanel, degreeRequirements.getText());
		    }
		});
		
		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				ManageDatabaseDialog.this.setVisible(false);
			}
		});
		
		donePanel = new JPanel();
		donePanel.add(done);

		// Build table panels
		try {
			/********************************************
			 * Courses panel
			 ********************************************/
			
			courseQTM = new QueryTableModel(ProgramState.url, "SELECT * FROM courses");
			
			courseQT = new QueryTable(courseQTM);
			
			addCourseDialog = new AddCourseDialog(this, courseQTM);
			addCourse = new JButton("Add Course");
			addCourse.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					addCourseDialog.setVisible(true);
				}
			});
			
			deleteCourse = new JButton("Delete Course");
			deleteCourse.addActionListener(new DeleteCourseActionListener(this, courseQT, courseQTM));
			
			updateCourseDialog = new UpdateCourseDialog(this, courseQTM, courseQT);
			updateCourse = new JButton("Update Course");
			updateCourse.addActionListener(new UpdateCourseDialogActionListener(this, courseQT, courseQTM, updateCourseDialog));
			
			courseSP = new JScrollPane(courseQT);
			
			cButtonPanel = new JPanel();
			cButtonPanel.add(addCourse);
			cButtonPanel.add(updateCourse);
			cButtonPanel.add(deleteCourse);
			
			coursePanel = new JPanel(new BorderLayout());
			coursePanel.add(cButtonPanel, BorderLayout.SOUTH);
			coursePanel.add(courseSP, BorderLayout.CENTER);

			/********************************************
			 * Degrees panel
			 ********************************************/
			
			degreeQTM = new QueryTableModel(ProgramState.url, "SELECT * FROM degree");
			
			degreeQT = new QueryTable(degreeQTM);
			
			addDegreeDialog = new AddDegreeDialog(this, degreeQTM);
			addDegree = new JButton("Add Degree");
			addDegree.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					addDegreeDialog.setVisible(true);
				}
			});

			deleteDegree = new JButton("Delete Degree");
			deleteDegree.addActionListener(new DeleteDegreeActionListener(this, degreeQT, degreeQTM));
			
			updateDegreeDialog = new UpdateDegreeDialog(this, degreeQTM, degreeQT);
			updateDegree = new JButton("Update Degree");
			updateDegree.addActionListener(new UpdateDegreeDialogActionListener(this, degreeQT, degreeQTM, updateDegreeDialog));
			
			degreeSP = new JScrollPane(degreeQT);

			dButtonPanel = new JPanel();
			dButtonPanel.add(addDegree);
			dButtonPanel.add(updateDegree);
			dButtonPanel.add(deleteDegree);
			
			degreePanel = new JPanel(new BorderLayout());
			degreePanel.add(dButtonPanel, BorderLayout.SOUTH);
			degreePanel.add(degreeSP, BorderLayout.CENTER);

			/********************************************
			 * Degree requirement panel
			 ********************************************/
			
			String query = "SELECT s.major, s.specialization, s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number)," +
					" s.selection_set_req FROM (SELECT major,specialization,courses.department,course_number,selection_set_id,selection_set_req FROM degree " +
					"NATURAL JOIN completion_req NATURAL JOIN selection_sets JOIN courses WHERE selection_sets.course_id=courses.course_id) AS s GROUP BY selection_set_id";
			
			degreeReqQTM = new CourseQueryTableModel(ProgramState.url, query);

			query = "SELECT s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number) FROM (SELECT selection_set_id,department,course_number " +
					"FROM selection_sets NATURAL JOIN courses ORDER BY course_number) AS s GROUP BY selection_set_id";
			
			selectionSetQTM = new CourseQueryTableModel(ProgramState.url, query);
			
			degreeReqQT = new QueryTable(degreeReqQTM);
			
			addSelSetDialog = new AddDegreeRequirementDialog(this, degreeReqQTM, selectionSetQTM);
			addDegreeReq = new JButton("Add Requirement");
			addDegreeReq.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					addSelSetDialog.clearCourseSelection();
					
					if(ProgramState.degreesUpdated) {
						addSelSetDialog.updateDegrees();
						
						// Since two views must have degrees updated in them, require 
						// an intermediate state for one beign updated and the other not.
						if(ProgramState.degreesUpdated2)
							ProgramState.degreesUpdated2 = false;
						else
							ProgramState.degreesUpdated = false;
					}
					addSelSetDialog.setVisible(true);
				}
			});

			deleteDegreeReq = new JButton("Delete Requirement");
			deleteDegreeReq.addActionListener(new DeleteDegreeRequirementActionListener(this, degreeReqQT, degreeReqQTM, selectionSetQTM));
			
			updateDegreeReqDialog = new UpdateDegreeRequirementDialog(this, degreeReqQT, degreeReqQTM, selectionSetQTM);
			updateDegreeReq = new JButton("Update Requirement");
			updateDegreeReq.addActionListener(new UpdateDegreeRequirementDialogActionListener(this, degreeReqQT, degreeReqQTM, updateDegreeReqDialog));
			
			degreeReqSP = new JScrollPane(degreeReqQT);

			dRButtonPanel = new JPanel();
			dRButtonPanel.add(addDegreeReq);
			dRButtonPanel.add(updateDegreeReq);
			dRButtonPanel.add(deleteDegreeReq);
			
			degreeReqPanel = new JPanel(new BorderLayout());
			degreeReqPanel.add(dRButtonPanel, BorderLayout.SOUTH);
			degreeReqPanel.add(degreeReqSP, BorderLayout.CENTER);

			/********************************************
			 * Course offerings panel
			 ********************************************/
			
			courseOfferingsPanel = new CourseOfferingsPanel(this);
			
			/********************************************
			 * TabedPane
			 ********************************************/
			
			tabPane = new JTabbedPane();
			tabPane.addTab("Courses", coursePanel);
			tabPane.addTab("Degrees", degreePanel);
			tabPane.addTab("Degree Requirements", degreeReqPanel);
			tabPane.addTab("Course Offerings", courseOfferingsPanel);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		this.add(tabPane, BorderLayout.CENTER);
		this.add(donePanel, BorderLayout.SOUTH);
		this.setMinimumSize(new Dimension(700, 500));
	}
	
	public void removePrereqFromChild(String key) {
		addCourseDialog.removePrereq(key);
	}
	
	public void updatePrereqFromChild(String key) {
		addCourseDialog.updatePrereq(key);
	}
}
