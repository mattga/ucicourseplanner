package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class UpdateDegreeDialogActionListener implements ActionListener {
	private JDialog parent;
	private QueryTable qt;
	private QueryTableModel qtm;
	private UpdateDegreeDialog udd;
	
	public UpdateDegreeDialogActionListener(JDialog parent, QueryTable qt, QueryTableModel qtm, UpdateDegreeDialog udd) {
		this.parent = parent;
		this.qtm = qtm;
		this.udd = udd;
		this.qt = qt;
	}

	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parent, "Must select a degree to update.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else
			try {
				ResultSet rs = qtm.getResultSet();
				rs.absolute(qt.getSelectedRow()+1);

				udd.setTexts(rs.getString(3), rs.getString(2), rs.getString(4));
				udd.setVisible(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

	}
}
