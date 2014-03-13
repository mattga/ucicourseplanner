package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.uci.ics.mattg.sql.QueryTableModel;

public class AddCourseActionListener implements ActionListener {
	private JDialog parent;
	private JTextField titleField, numberField, departmentField, creditsField;
	private Iterable<CourseItem> prereqs;
	private QueryTableModel qtm;
	protected Connection connection;
	protected Statement statement;
	protected ResultSet rs;

	public AddCourseActionListener(JDialog parent, JTextField titleField, JTextField numberField, JTextField departmentField, JTextField creditsField, Iterable<CourseItem> prereqs, QueryTableModel qtm) {
		this.titleField = titleField;
		this.numberField = numberField;
		this.departmentField = departmentField;
		this.creditsField = creditsField;
		this.prereqs = prereqs;
		this.qtm = qtm;
		this.parent = parent;

		try {
			Class.forName(ProgramState.jdbcDriver);
			connection = DriverManager.getConnection(ProgramState.url);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actionPerformed (ActionEvent e) {
		String title, number, department, credits;
		title = titleField.getText();
		number = numberField.getText();
		department = departmentField.getText();
		credits = creditsField.getText();
		
		if(title.replaceFirst("[\\s+]", "").equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the title to create a course.", "Course Title Required", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(number.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the number to create a course.", "Course Number Required", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(department.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the department to create a course.", "Course Department Required", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(credits.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the credits to create a course.", "Course Credits Required", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "INSERT INTO courses (course_title, course_number, department, credits) VALUES ('";
		query += title + "', '" + number + "', '" + department + "', '" + credits + "')";
		System.out.println(query);
		
		
		Logger.getLogger("edu.uci.ics.mattg.CourseScheduler").info("Adding course item '" + department + " " + number + " " + title + "'");

		try {
			statement.executeUpdate(query);
			((AddCourseDialog)this.parent).addPrereq(new CourseItem(department, number, title, Integer.parseInt(credits), ""));
			ProgramState.coursesUpdated = true;
			qtm.executeQuery("SELECT * FROM courses");
			rs = statement.executeQuery("SELECT course_id FROM courses WHERE course_number='" + number + "' AND department='" + department + "' AND course_number='" + number + "'");
			rs.last();
			int id = rs.getInt(1);
			
			CourseItem newItem = new CourseItem(department, number, title, Integer.parseInt(credits), "");

			String key = (department + number + ":" + title).toLowerCase().replaceAll(" ", "");
			ProgramState.courseMap.put(key, newItem);

			for(CourseItem item : prereqs)
				if (item.isSelected()) {
					query = "INSERT INTO prereqs (course_id, prereq_id) SELECT '" + id + "', course_id FROM courses WHERE department='" + item.getDepartment() + "' AND course_number='" + number + "'";
					System.out.println(query);
					statement.executeUpdate(query);
				}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		titleField.setText("");
		numberField.setText("");
		departmentField.setText("");
		creditsField.setText("");
		parent.setVisible(false);
		
		((AddCourseDialog)parent).clearPrereqSelection();
	}
}
