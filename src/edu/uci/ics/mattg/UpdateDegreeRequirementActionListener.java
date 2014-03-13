package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.uci.ics.mattg.sql.QueryTableModel;

public class UpdateDegreeRequirementActionListener implements ActionListener {
	private JComboBox<String> degreeComboBox;
	private JTextField amountField;
	private Iterable<CourseItem> selectionSet;
	private QueryTableModel qtm, ssQtm;
	private JDialog parent;
	private Connection connection;
	private Statement statement;

	public UpdateDegreeRequirementActionListener(JDialog parent, JComboBox<String> degreeComboBox, JTextField amountField, Iterable<CourseItem> selectionSet, QueryTableModel qtm, QueryTableModel ssQtm) {
		this.degreeComboBox = degreeComboBox;
		this.amountField = amountField;
		this.selectionSet = selectionSet;
		this.qtm = qtm;
		this.ssQtm = ssQtm;
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
		String[] degree = ((String)degreeComboBox.getSelectedItem()).split(">");
		String specialization = (degree.length > 4 ? degree[4] : "");
		String major = degree[3];
		if (!specialization.equals("")) {
			major = major.substring(0, degree[3].length()-4);
			specialization = specialization.substring(1, degree[4].length());
		}
		String amount = amountField.getText();

		try {
			int setSize = 0;
			for(CourseItem c : selectionSet)
				if(c.isSelected())
					setSize++;
			System.out.println(setSize);
			
			if(amount.equals("")) {
				JOptionPane.showMessageDialog(parent, "Please enter the amount of classes required to be taken.", "Amount Required", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(setSize < Integer.parseInt(amount)) {
				JOptionPane.showMessageDialog(parent, "Selection set does not contain enough courses for the amount required to be taken.", "Inadequate Selection Set", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Delete old degree requirement
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

			// Add new degree requirement
			query = "INSERT INTO completion_req (degree_id,selection_set_req) SELECT degree_id,'" + amount + "' FROM degree WHERE major='" + major + "' AND specialization='" + specialization + "'";
			System.out.println(query);

			statement.executeUpdate(query);

			System.out.println("SELECT selection_set_id FROM degree NATURAL JOIN completion_req WHERE major='" + major + "' AND specialization='" + specialization + "'");
			rs = statement.executeQuery("SELECT selection_set_id FROM degree NATURAL JOIN completion_req WHERE major='" + major + "' AND specialization='" + specialization + "'");
			rs.last();
			int newSsID = rs.getInt(1);

			for(CourseItem item : selectionSet)
				if (item.isSelected()) {
					query = "INSERT INTO selection_sets (selection_set_id, course_id) SELECT '" + newSsID + "', course_id FROM courses WHERE department='" + item.getDepartment() + "' AND course_number='" + item.getNumber() + "'";
					System.out.println(query);
					statement.executeUpdate(query);
				}

			qtm.executeQuery("SELECT s.major, s.specialization, selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number), " +
					"s.selection_set_req FROM (SELECT major,specialization,courses.department,course_number,selection_set_id,selection_set_req FROM degree " +
					"NATURAL JOIN completion_req NATURAL JOIN selection_sets JOIN courses WHERE selection_sets.course_id=courses.course_id) AS s GROUP BY selection_set_id");
			ssQtm.executeQuery("SELECT s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number) FROM (SELECT " +
					"selection_set_id,department,course_number FROM selection_sets NATURAL JOIN courses ORDER BY course_number) AS s GROUP BY selection_set_id");
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		degreeComboBox.setSelectedIndex(0);
		amountField.setToolTipText("");
		parent.setVisible(false);
	}
}
