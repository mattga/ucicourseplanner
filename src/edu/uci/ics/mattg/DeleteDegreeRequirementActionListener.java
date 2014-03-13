package edu.uci.ics.mattg;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class DeleteDegreeRequirementActionListener implements ActionListener {
	private QueryTableModel qtm, ssQtm;
	private Component parentComponent;
	private QueryTable qt;
	private Connection connection;
	private Statement statement;
	
	public DeleteDegreeRequirementActionListener(Component parentComponent, QueryTable qt, QueryTableModel qtm, QueryTableModel ssQtm) {
		this.qtm = qtm;
		this.qt = qt;
		this.ssQtm = ssQtm;
		this.parentComponent = parentComponent;
		
		try {
			Class.forName(ProgramState.jdbcDriver);
			connection = DriverManager.getConnection(ProgramState.url);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parentComponent, "Must select a requirement to delete.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else if (JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete this requirement?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
			try {
				ResultSet rs = qtm.getResultSet();

				int ssID = rs.getInt(3);

				String query = "DELETE FROM selection_sets WHERE selection_set_id='" + ssID + "'";
				System.out.println(query);
				int res = statement.executeUpdate(query);
				if (res == 0)
					System.err.println("selection_set delete failed");

				query = "DELETE FROM completion_req WHERE selection_set_id='" + ssID + "'";
				System.out.println(query);
				res = statement.executeUpdate(query);
				if (res == 0)
					System.err.println("completion_req delete failed");

				qtm.executeQuery("SELECT s.major, s.specialization, selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number), " +
						"s.selection_set_req FROM (SELECT major,specialization,courses.department,course_number,selection_set_id,selection_set_req FROM degree " +
						"NATURAL JOIN completion_req NATURAL JOIN selection_sets JOIN courses WHERE selection_sets.course_id=courses.course_id) AS s GROUP BY selection_set_id");
				ssQtm.executeQuery("SELECT s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number) FROM (SELECT " +
						"selection_set_id,department,course_number FROM selection_sets NATURAL JOIN courses ORDER BY course_number) AS s GROUP BY selection_set_id");
				ProgramState.coursesUpdated = true;
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
