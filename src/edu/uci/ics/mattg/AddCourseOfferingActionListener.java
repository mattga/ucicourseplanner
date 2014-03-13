package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class AddCourseOfferingActionListener implements ActionListener {
	private JComboBox<String> quarterComboBox;
	private JComboBox<Integer> yearComboBox;
	private Iterable<CourseItem> courses;
	private CourseQueryTableModel ctm;
	private JDialog parent;
	private Connection connection;
	private Statement statement;

	public AddCourseOfferingActionListener(JDialog parent, JComboBox<String> quarterComboBox, JComboBox<Integer> yearComboBox, Iterable<CourseItem> courses, CourseQueryTableModel ctm) {
		this.quarterComboBox = quarterComboBox;
		this.yearComboBox = yearComboBox;
		this.courses = courses;
		this.ctm = ctm;
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
		for (AbstractButton b : courses) {
			if (b.isSelected()) {
				String[] almostCourse = (b.getText()).split(">");
				String[] course = almostCourse[3].substring(0, almostCourse[3].length()-4).split(" ");
				String department = course[0]; 
				String courseNumber = course[1];

				String query = "INSERT INTO course_offerings (course_id, year, quarter) SELECT course_id, '";
				query += "" + yearComboBox.getSelectedItem() + "', '";
				String query2 = (String)quarterComboBox.getSelectedItem() + "' FROM courses WHERE department='";
				query2 += department + "' AND course_number='" + courseNumber + "'";
				
				try {
					System.out.println(query + query2);
					statement.executeUpdate(query + query2);
					ctm.executeQuery(ProgramState.getProjectedCoursesQuery());	
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		parent.setVisible(false);
	}
}
