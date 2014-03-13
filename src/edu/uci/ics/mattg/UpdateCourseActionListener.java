package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JTextField;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class UpdateCourseActionListener implements ActionListener {
	private JTextField titleField, numberField, departmentField, creditsField;
	private QueryTableModel qtm;
	private QueryTable qt;
	private Connection connection;
	private Statement statement;
	private JDialog parent;
	
	public UpdateCourseActionListener(JDialog parent, JTextField titleField, JTextField numberField, JTextField departmentField, JTextField creditsField, QueryTableModel qtm, QueryTable qt) {
		this.titleField = titleField;
		this.numberField = numberField;
		this.departmentField = departmentField;
		this.creditsField = creditsField;
		this.qtm = qtm;
		this.qt = qt;
		this.parent = parent;

		try {
			Class.forName(ProgramState.jdbcDriver);
			connection = DriverManager.getConnection(ProgramState.url);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String title, number, department, credits;
		title = titleField.getText();
		number = numberField.getText();
		department = departmentField.getText();
		credits = creditsField.getText();
		
		try {
			ResultSet rs = qtm.getResultSet();
			rs.absolute(qt.getSelectedRow()+1);
			int id = rs.getInt(1);
			String numberOld = rs.getString(3);
			String departmentOld = rs.getString(2);
			String titleOld = rs.getString(4);

			CourseItem item = new CourseItem(department, number, title, rs.getInt(5), rs.getString(6));
			String oldkey = (departmentOld + numberOld + ":" + titleOld).toLowerCase().replaceAll(" ", "");
			String newkey = (department + number + ":" + title).toLowerCase().replaceAll(" ", "");
			ProgramState.courseMap.remove(oldkey);
			ProgramState.courseMap.put(newkey, item);
			
			String query = "UPDATE courses SET ";
			query += "course_title='" + title + "', course_number='" + number + "', department='" + department + "', credits='" + credits + "' WHERE course_id='" + id + "'";
			System.out.println(query);


			statement.executeUpdate(query);
			qtm.executeQuery("SELECT * FROM courses");
			ProgramState.coursesUpdated = true;

		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

		titleField.setText("");
		numberField.setText("");
		departmentField.setText("");
		creditsField.setText("");
		parent.setVisible(false);
	}
}
