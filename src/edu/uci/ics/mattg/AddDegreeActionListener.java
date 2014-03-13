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

public class AddDegreeActionListener implements ActionListener {
	private JDialog parent;
	private JTextField majorField, specField, departmentField;
	private QueryTableModel qtm;
	protected Connection connection;
	protected Statement statement;
	protected ResultSet rs;

	public AddDegreeActionListener(JDialog parent, JTextField majorField, JTextField specField, JTextField departmentField, QueryTableModel qtm) {
		this.parent= parent;
		this.majorField = majorField;
		this.specField = specField;
		this.departmentField = departmentField;
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
		String major, spec, department;
		major = majorField.getText();
		spec = specField.getText();
		department = departmentField.getText();

		if(major.replaceFirst("[\\s+]", "").equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the major to create a degree.", "Degree Major Required", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(spec.replaceFirst("[\\s+]", "").equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the specialization to create a degree.", "Degree Specialization Required", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(department.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the department to create a degree.", "Course Department Required", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String query = "INSERT INTO degree (major, specialization, department) VALUES ('";
		query += major + "', '" + spec + "', '" + department + "')";
		System.out.println(query);

		Logger.getLogger("edu.uci.ics.mattg.CourseScheduler").info("Adding degree item '" + department + " " + major + " " + spec + "'");

		try {
			statement.executeUpdate(query);
			qtm.executeQuery("SELECT * FROM degree");
			ProgramState.degreesUpdated = true;
			ProgramState.degreesUpdated2 = true;

			DegreeItem newItem = new DegreeItem(major, spec, department);

			String key = (major + spec).toLowerCase().replaceAll(" ", "");
			ProgramState.degreeMap.put(key, newItem);

		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		majorField.setText("");
		specField.setText("");
		departmentField.setText("");
		parent.setVisible(false);
	}
}
