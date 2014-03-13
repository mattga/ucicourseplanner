package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class UpdateCourseDialogActionListener implements ActionListener {
	private JDialog parent;
	private QueryTable qt;
	private QueryTableModel qtm;
	private UpdateCourseDialog ucd;
	
	public UpdateCourseDialogActionListener(JDialog parent, QueryTable qt, QueryTableModel qtm, UpdateCourseDialog ucd) {
		this.parent = parent;
		this.qtm = qtm;
		this.ucd = ucd;
		this.qt = qt;
	}

	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parent, "Must select a course to update.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else
			try {
				ResultSet rs = qtm.getResultSet();
				rs.absolute(qt.getSelectedRow()+1);

				ucd.setTexts(rs.getString(3), rs.getString(4), rs.getString(2),"" + rs.getString(5));
				ucd.setVisible(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

	}
}
