package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.uci.ics.mattg.sql.QueryTable;

public class DegreeRequirementsDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel specializationSelectionLabel;
	private JPanel specializationSelectionPanel, buttonPanel;
	private JButton done;
	private JComboBox<String> degreeComboBox;
	private QueryTable qt;
	private CourseQueryTableModel ctm;
	private String query1, query2, query3;
	private String[] degrees;

	public DegreeRequirementsDialog(JFrame parent) {
		super(parent, true);

		int i = 0;
		degrees = new String[ProgramState.degreeMap.size()];
		for (DegreeItem s : ProgramState.degreeMap.values())
			degrees[i++] = s.getText();
		
		query1 = "SELECT group_concat(s.department,' ',s.course_number ORDER BY s.course_number), s.selection_set_req" +
				" FROM (SELECT major,specialization,courses.department,course_number,selection_set_id,selection_set_req FROM " +
				"degree NATURAL JOIN completion_req NATURAL JOIN selection_sets JOIN courses WHERE selection_sets.course_id=courses.course_id AND" +
				" selection_sets.course_id=courses.course_id AND major='";
		query2 = "' AND specialization='";
		query3 = "' ORDER BY courses.course_number) AS s GROUP BY selection_set_id";
		
		degreeComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(degrees));
		degreeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				String[] almostSpecialization = ((String)degreeComboBox.getSelectedItem()).split(">");
				String specialization, major; 

				try {
					specialization = (almostSpecialization.length > 4 ? almostSpecialization[4] : "");
					major = almostSpecialization[3];
					if (!specialization.equals("")) {
						major = major.substring(0, almostSpecialization[3].length()-4);
						specialization = specialization.substring(1, almostSpecialization[4].length());
					}
					System.out.println(query1+major+query2+specialization+query3);
					ctm.execute(query1+major+query2+specialization+query3);

				} catch (SQLException ex2) {
					ex2.printStackTrace();
				}
			}
		});
		
		String[] almostSpecialization;
		String major = "", specialization = "";
		try {
			almostSpecialization = ((String)degreeComboBox.getSelectedItem()).split(">");
			specialization = (almostSpecialization.length > 4 ? almostSpecialization[4] : "");
			major = almostSpecialization[3];
			if (!specialization.equals("")) {
				major = major.substring(0, almostSpecialization[3].length()-4);
				specialization = specialization.substring(1, almostSpecialization[4].length());
			}

			System.out.println(query1+major+query2+specialization+query3);
			ctm = new CourseQueryTableModel(ProgramState.url, query1+major+query2 +specialization+query3);
			qt = new QueryTable(ctm);
		} catch (ArrayIndexOutOfBoundsException ex1) {
			specialization = " IS NULL";
			try {
				System.out.println(query1+major+query2+specialization+query3);
				ctm = new CourseQueryTableModel(ProgramState.url, query1+major+query2+specialization+query3);
				qt = new QueryTable(ctm);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}

		specializationSelectionLabel = new JLabel("Specialization:");

		specializationSelectionPanel = new JPanel();
		specializationSelectionPanel.add(specializationSelectionLabel);
		specializationSelectionPanel.add(degreeComboBox);

		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DegreeRequirementsDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(done);

		this.add(specializationSelectionPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(qt), BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setTitle("Completion Requirements");
		this.setMinimumSize(new Dimension(600,400));
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getClassLoader().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public void updateDegrees() {
		int i = 0;
		degrees = new String[ProgramState.degreeMap.size()];
		for (DegreeItem s : ProgramState.degreeMap.values())
			degrees[i++] = s.getText();
		this.revalidate();
		this.repaint();
	}
}