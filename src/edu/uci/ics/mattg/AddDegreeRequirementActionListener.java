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

public class AddDegreeRequirementActionListener implements ActionListener {
	private JDialog parent;
	private JTextField dReqAmountField;
	private Iterable<CourseItem> courses;
	private JComboBox<String> degreeComboBox;
	private QueryTableModel qtm, ssQtm;
	protected Connection connection;
	protected Statement statement;
	protected ResultSet rs;

	public AddDegreeRequirementActionListener(JDialog parent, JComboBox<String> degreeComboBox, JTextField dReqAmountField, Iterable<CourseItem> courses, QueryTableModel qtm, QueryTableModel ssQtm) {
		this.degreeComboBox = degreeComboBox;
		this.dReqAmountField = dReqAmountField;
		this.courses = courses;
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
		String degree, amount;
		degree = (String)degreeComboBox.getSelectedItem();
		amount = dReqAmountField.getText();
		String[] almostDegree = degree.split(">");
		String major = almostDegree[3];
		String specialization = (almostDegree.length > 4 ? almostDegree[4] : "");
		System.out.println(degree + "\n" + major + "\n" + specialization);
		if (!specialization.equals("")) {
			major = major.substring(0, almostDegree[3].length()-4);
			specialization = specialization.substring(1, almostDegree[4].length());
		}
		int setSize = 0;
		for(CourseItem c : courses)
			if(c.isSelected())
				setSize++;
		
		if(amount.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter the amount of classes required to be taken.", "Amount Required", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(setSize < Integer.parseInt(amount)) {
			JOptionPane.showMessageDialog(parent, "Selection set does not contain enough courses for the amount required to be taken.", "Inadequate Selection Set", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "INSERT INTO completion_req (degree_id,selection_set_req) SELECT degree_id,'" + amount + "' FROM degree WHERE major='" + major + "' AND specialization='" + specialization + "'";
		System.out.println(query);
		
		try {
			statement.executeUpdate(query);

			rs = statement.executeQuery("SELECT selection_set_id FROM degree NATURAL JOIN completion_req WHERE major='" + major + "' AND specialization='" + specialization + "'");
			rs.last();
			int ssID = rs.getInt(1);

			for(CourseItem item : courses)
				if (item.isSelected()) {
					query = "INSERT INTO selection_sets (selection_set_id, course_id) SELECT '" + ssID + "', course_id FROM courses WHERE department='" + item.getDepartment() + "' AND course_number='" + item.getNumber() + "'";
					System.out.println(query);
					statement.executeUpdate(query);
				}

			qtm.executeQuery("SELECT s.major, s.specialization, s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number), " +
					"s.selection_set_req FROM (SELECT major,specialization,courses.department,course_number,selection_set_id,selection_set_req FROM degree " +
					"NATURAL JOIN completion_req NATURAL JOIN selection_sets JOIN courses WHERE selection_sets.course_id=courses.course_id) AS s GROUP BY selection_set_id");
			ssQtm.executeQuery("SELECT s.selection_set_id, group_concat(s.department,' ',s.course_number ORDER BY s.course_number) FROM (SELECT " +
					"selection_set_id,department,course_number FROM selection_sets NATURAL JOIN courses ORDER BY course_number) AS s GROUP BY selection_set_id");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		degreeComboBox.setSelectedIndex(0);
		dReqAmountField.setText("");
		parent.setVisible(false);
	}
}
