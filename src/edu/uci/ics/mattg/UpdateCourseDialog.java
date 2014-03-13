package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;
import edu.uci.ics.mattg.ui.RegexTextField;

public class UpdateCourseDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton ok, cancel;
	private JPanel buttonPanel, coursePanel;
	private JTextField courseTitle, courseNumber, department, courseCredits;
	private JLabel courseInfoLabel, courseTitleLabel, courseNumberLabel, departmentLabel, courseCreditsLabel;

	public UpdateCourseDialog(JDialog parent, QueryTableModel qtm, QueryTable qt) {
		super(parent, "Update Course");

		courseInfoLabel = new JLabel("Course Information");
		courseInfoLabel.setFont(new Font(courseInfoLabel.getFont().getName(), Font.BOLD, 11));

		courseTitleLabel = new JLabel("Course Title:");

		courseTitle = new JTextField(17);

		courseNumberLabel = new JLabel("Course Number:");

		courseNumber = new RegexTextField(5, "[a-zA-Z0-9]+", 4);

		departmentLabel = new JLabel("Department:");

		department = new RegexTextField(5, "[a-zA-Z]+", 5);

		courseCreditsLabel = new JLabel("Course Credits:");

		courseCredits = new RegexTextField(3, "[0-9]+", 3);

		ok = new JButton("Ok");
		ok.addActionListener(new UpdateCourseActionListener(this, courseTitle, courseNumber, department, courseCredits, qtm, qt));

		SpringLayout springLayout = new SpringLayout();
		coursePanel = new JPanel(springLayout);
		coursePanel.add(courseInfoLabel);
		coursePanel.add(courseTitleLabel);
		coursePanel.add(courseTitle);
		coursePanel.add(courseNumberLabel);
		coursePanel.add(courseNumber);
		coursePanel.add(departmentLabel);
		coursePanel.add(department);
		coursePanel.add(courseCreditsLabel);
		coursePanel.add(courseCredits);

		configureSpringLayout(springLayout);

		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateCourseDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		this.setMinimumSize(new Dimension(330, 200));
		this.setResizable(true);
		this.add(coursePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void configureSpringLayout(SpringLayout springLayout) {

		/** SprimgLayout Constraints **/
		// courseInfoLabel
		springLayout.putConstraint(SpringLayout.NORTH, courseInfoLabel, 10, SpringLayout.NORTH, coursePanel);
		springLayout.putConstraint(SpringLayout.WEST, courseInfoLabel, 20, SpringLayout.WEST, coursePanel);

		// courseTitleLabel
		springLayout.putConstraint(SpringLayout.NORTH, courseTitleLabel, 10, SpringLayout.SOUTH, courseInfoLabel);
		springLayout.putConstraint(SpringLayout.WEST, courseTitleLabel, 20, SpringLayout.WEST, coursePanel);

		// courseTitle
		springLayout.putConstraint(SpringLayout.NORTH, courseTitle, -3, SpringLayout.NORTH, courseTitleLabel);
		springLayout.putConstraint(SpringLayout.WEST, courseTitle, 10, SpringLayout.EAST, courseTitleLabel);

		// courseNumberLabel
		springLayout.putConstraint(SpringLayout.NORTH, courseNumberLabel, 10, SpringLayout.SOUTH, courseTitleLabel);
		springLayout.putConstraint(SpringLayout.EAST, courseNumberLabel, 0, SpringLayout.EAST, courseTitleLabel);

		// courseNumber
		springLayout.putConstraint(SpringLayout.NORTH, courseNumber, -3, SpringLayout.NORTH, courseNumberLabel);
		springLayout.putConstraint(SpringLayout.WEST, courseNumber, 10, SpringLayout.EAST, courseNumberLabel);

		// departmentLabel
		springLayout.putConstraint(SpringLayout.NORTH, departmentLabel, 10, SpringLayout.SOUTH, courseNumberLabel);
		springLayout.putConstraint(SpringLayout.EAST, departmentLabel, 0, SpringLayout.EAST, courseTitleLabel);

		// department
		springLayout.putConstraint(SpringLayout.NORTH, department, -3, SpringLayout.NORTH, departmentLabel);
		springLayout.putConstraint(SpringLayout.WEST, department, 10, SpringLayout.EAST, departmentLabel);

		// courseCreditsLabel
		springLayout.putConstraint(SpringLayout.NORTH, courseCreditsLabel, 10, SpringLayout.SOUTH, departmentLabel);
		springLayout.putConstraint(SpringLayout.EAST, courseCreditsLabel, 0, SpringLayout.EAST, courseTitleLabel);

		// courseCredits
		springLayout.putConstraint(SpringLayout.NORTH, courseCredits, -3, SpringLayout.NORTH, courseCreditsLabel);
		springLayout.putConstraint(SpringLayout.WEST, courseCredits, 10, SpringLayout.EAST, courseCreditsLabel);
	}

	public void setTexts(String number, String title, String department, String credits) {
		courseNumber.setText(number);
		courseTitle.setText(title);
		this.department.setText(department);
		courseCredits.setText(credits);
	}
}
