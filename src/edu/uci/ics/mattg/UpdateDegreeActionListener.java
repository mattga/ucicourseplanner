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

public class UpdateDegreeActionListener implements ActionListener {
	private JTextField majorField, specField, departmentField;
	private QueryTableModel qtm;
	private QueryTable qt;
	private Connection connection;
	private Statement statement;
	private JDialog parent;
	
	public UpdateDegreeActionListener(JDialog parent, JTextField majorField, JTextField specField, JTextField departmentField, QueryTableModel qtm, QueryTable qt) {
		this.majorField = majorField;
		this.specField = specField;
		this.departmentField = departmentField;
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
	
	public void actionPerformed (ActionEvent e) {
		String major, spec, department;
		major = majorField.getText();
		spec = specField.getText();
		department = departmentField.getText();
		
		try {
			ResultSet rs = qtm.getResultSet();
			rs.absolute(qt.getSelectedRow()+1);
			int id = rs.getInt(1);
			String specOld = rs.getString(2);
			String majorOld = rs.getString(3);
			
			DegreeItem item = new DegreeItem(major, spec, department);
			String oldkey = (majorOld + specOld).toLowerCase().replaceAll(" ", "");
			String newkey = (major + spec).toLowerCase().replaceAll(" ", "");
			ProgramState.degreeMap.remove(oldkey);
			ProgramState.degreeMap.put(newkey, item);
			
			String query = "UPDATE degree SET ";
			query += "major='" + major + "', specialization='" + spec + "', department='" + department + "' WHERE degree_id='" + id + "'";
			System.out.println(query);

			statement.executeUpdate(query);
			qtm.executeQuery("SELECT * FROM degree");
			ProgramState.degreesUpdated = true;
			ProgramState.degreesUpdated2 = true;

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
