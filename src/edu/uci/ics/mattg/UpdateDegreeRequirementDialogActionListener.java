package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;

public class UpdateDegreeRequirementDialogActionListener implements ActionListener {
	private JDialog parent;
	private QueryTable qt;
	private QueryTableModel qtm;
	private UpdateDegreeRequirementDialog udrd;
	
	public UpdateDegreeRequirementDialogActionListener(JDialog parent, QueryTable qt, QueryTableModel qtm, UpdateDegreeRequirementDialog udrd) {
		this.parent = parent;
		this.qtm = qtm;
		this.udrd = udrd;
		this.qt = qt;
	}

	public void actionPerformed (ActionEvent e) {
		if (qt.getSelectedRow() < 0)
			JOptionPane.showMessageDialog(parent, "Must select a degree requirememt to update.", "No row selected", JOptionPane.ERROR_MESSAGE);
		else
			try {
				ResultSet rs = qtm.getResultSet();
				rs.absolute(qt.getSelectedRow()+1);
				
				String major = rs.getString(1);
				String specialization = rs.getString(2);
				String s = "<b>" + major + (specialization.equals("") ? "" : ":</b> " + specialization);
				String html1 = "<html><body style='width: ";
				String html2 = "px'>";
				udrd.setTexts(html1+ProgramState.DEGREE_ITEM_WIDTH+html2+s, rs.getInt(5), rs.getString(4));
				udrd.setVisible(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

	}
}
