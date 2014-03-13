package edu.uci.ics.mattg;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;

public class DeleteCourseOfferingActionListener implements ActionListener {
	private CourseQueryTableModel ctm;
	private Component parentComponent;
	private QueryTable qt;
	private ButtonGroup quarter;
	private JLabel year;
	private Connection connection;
	private Statement statement;

	public DeleteCourseOfferingActionListener(Component parentComponent, QueryTable qt, CourseQueryTableModel ctm, JLabel year, ButtonGroup quarter) {
		this.ctm = ctm;
		this.qt = qt;
		this.parentComponent = parentComponent;
		this.year = year;
		this.quarter = quarter;
		
		try {
			Class.forName(ProgramState.jdbcDriver);
			connection = DriverManager.getConnection(ProgramState.url);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() == -1)
			JOptionPane.showMessageDialog(parentComponent, "Must select a course to delete", "No row selected", JOptionPane.ERROR_MESSAGE);
		else if (JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete the selected course offering(s)?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
			String query;

			try {
				int[] indices = qt.getSelectedRows();
				ResultSet rs = ctm.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				System.out.println("length:" + indices.length);
				for(int i = 0; i < indices.length; i++) {
					query = "DELETE course_offerings FROM course_offerings NATURAL JOIN courses WHERE ";
					System.out.print(indices[i]+1 + ",");
					rs.absolute(indices[i]+1);

					// Three columns to identify the quarter by...
					for (int j = 0; j < 3; j++)
						query += rsmd.getColumnName(j+1) + "='" + rs.getObject(j+1) + "' AND ";
					query += "year='" + (int)Integer.parseInt(year.getText()) + "' AND quarter='" + ((CourseTableButtonModel)quarter.getSelection()).getText() + "'";

					System.out.println(query);
					statement.executeUpdate(query);
				}
				ctm.executeQuery(ProgramState.getProjectedCoursesQuery());
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
