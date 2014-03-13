package edu.uci.ics.mattg;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.collections.Map;
import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class DeleteCourseActionListener implements ActionListener {
	private QueryTableModel qtm;
	private Component parentComponent;
	private QueryTable qt;
	private Connection connection;
	private Statement statement;

	public DeleteCourseActionListener(Component parentComponent, QueryTable qt, QueryTableModel qtm) {
		this.qtm = qtm;
		this.qt = qt;
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
			JOptionPane.showMessageDialog(parentComponent, "Must select a course to delete.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else if (JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete this course?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
			String query;
			String key = "";
			Map<Integer,String> failed = new ArrayMap<Integer,String>();

			try {
				int[] indices = qt.getSelectedRows();
				ResultSet rs = qtm.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				for(int i = 0; i < indices.length; i++) {
					System.out.print(indices[i]+1 + ",");
					rs.absolute(indices[i]+1);
					query = "DELETE FROM courses WHERE ";
					key = (rs.getString("department") + rs.getString("course_number") + ":" + rs.getString("course_title")).toLowerCase().replaceAll(" ", "");
					ProgramState.courseMap.remove(key);

					// Build DELETE query on all equivalent attributes except description
					for (int j = 0; j < rsmd.getColumnCount() - 1; j++)
						query += (j == 0 ? "" : " AND ") + rsmd.getColumnName(j+1) + "='" + rs.getObject(j+1) + "'";


					System.out.println(query);
					try {
						statement.executeUpdate(query);
						((ManageDatabaseDialog)parentComponent).removePrereqFromChild(key);
					} catch (MySQLIntegrityConstraintViolationException ex) {
						failed.put(Integer.valueOf(rs.getInt(1)), rs.getString("department") + rs.getString("course_number") + "cannot be deleted due to an integrity constraint from prereqs.");
					}
				}
				if(failed.size() > 0) {
					String error = "";
					for(Map.Entry<Integer,String> me : failed.entries())
						error += "Course ID " + me.getKey() + ": " + me.getValue() + "\n";

					JTextArea jta = new JTextArea(error);
					JScrollPane jsp = new JScrollPane(jta){
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						public Dimension getPreferredSize() {
							return new Dimension(480, 320);
						}
					};
					JOptionPane.showMessageDialog(null, jsp, "Failed to delete", JOptionPane.ERROR_MESSAGE);
				}

				qtm.executeQuery("SELECT * FROM courses");
				ProgramState.coursesUpdated = true;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
