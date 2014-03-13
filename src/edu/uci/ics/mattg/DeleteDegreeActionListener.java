package edu.uci.ics.mattg;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class DeleteDegreeActionListener implements ActionListener {
	private QueryTableModel qtm;
	private Component parentComponent;
	private QueryTable qt;
	
	public DeleteDegreeActionListener(Component parentComponent, QueryTable qt, QueryTableModel qtm) {
		this.qtm = qtm;
		this.qt = qt;
		this.parentComponent = parentComponent;
	}
	
	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parentComponent, "Must select a degree to delete.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else if (JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete this degree?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
			String query = "DELETE FROM degree WHERE ";

			try {
				ResultSet rs = qtm.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				String key = (rs.getString("major") + rs.getString("specialization")).toLowerCase().replaceAll(" ", "");
				ProgramState.degreeMap.remove(key);
				// Build DELETE query on all equivalent attributes except description
				for (int i = 0; i < rsmd.getColumnCount() - 1; i++)
					query += (i == 0 ? "" : " AND ") + rsmd.getColumnName(i+1) + "='" + rs.getObject(i+1) + "'";

				System.out.println(query);
				qtm.executeUpdate(query);
				qtm.executeQuery("SELECT * FROM degree");
				ProgramState.degreesUpdated = true;
				ProgramState.degreesUpdated2 = true;
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
