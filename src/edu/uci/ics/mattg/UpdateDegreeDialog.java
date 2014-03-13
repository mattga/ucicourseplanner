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

public class UpdateDegreeDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton ok, cancel;
	private JPanel buttonPanel, degreePanel;
	private JTextField degreeMajor, degreeSpec, department;
	private JLabel degreeInfoLabel, degreeMajorLabel, degreeSpecLabel, departmentLabel;
	public UpdateDegreeDialog(JDialog parent, QueryTableModel qtm, QueryTable qt) {
		super(parent, "Update Degree");

		degreeInfoLabel = new JLabel("Degree Information");
		degreeInfoLabel.setFont(new Font(degreeInfoLabel.getFont().getName(), Font.BOLD, 11));

		degreeMajorLabel = new JLabel("Major:");

		degreeMajor = new JTextField(20);

		degreeSpecLabel = new JLabel("Specialization:");

		degreeSpec = new JTextField(20);

		departmentLabel = new JLabel("Department:");

		department = new RegexTextField(5, "[a-z,A-Z]+", 5);

		ok = new JButton("Ok");
		ok.addActionListener(new UpdateDegreeActionListener(this, degreeMajor, degreeSpec, department, qtm, qt));

		SpringLayout springLayout = new SpringLayout();
		degreePanel = new JPanel(springLayout);
		degreePanel.add(degreeInfoLabel);
		degreePanel.add(degreeMajorLabel);
		degreePanel.add(degreeMajor);
		degreePanel.add(degreeSpecLabel);
		degreePanel.add(degreeSpec);
		degreePanel.add(departmentLabel);
		degreePanel.add(department);

		configureSpringLayout(springLayout);

		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateDegreeDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		this.setMinimumSize(new Dimension(350, 190));
		this.setResizable(true);
		this.add(degreePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	void configureSpringLayout(SpringLayout springLayout) {

		/** SprimgLayout Constraints **/
		// degreeInfoLabel
		springLayout.putConstraint(SpringLayout.NORTH, degreeInfoLabel, 10, SpringLayout.NORTH, degreePanel);
		springLayout.putConstraint(SpringLayout.WEST, degreeInfoLabel, 20, SpringLayout.WEST, degreePanel);

		// degreeMajorLabel
		springLayout.putConstraint(SpringLayout.NORTH, degreeMajorLabel, 10, SpringLayout.SOUTH, degreeInfoLabel);
		springLayout.putConstraint(SpringLayout.EAST, degreeMajorLabel, 0, SpringLayout.EAST, degreeSpecLabel);

		// degreeMajor
		springLayout.putConstraint(SpringLayout.NORTH, degreeMajor, -3, SpringLayout.NORTH, degreeMajorLabel);
		springLayout.putConstraint(SpringLayout.WEST, degreeMajor, 10, SpringLayout.EAST, degreeMajorLabel);

		// degreeSpecLabel
		springLayout.putConstraint(SpringLayout.NORTH, degreeSpecLabel, 10, SpringLayout.SOUTH, degreeMajorLabel);
		springLayout.putConstraint(SpringLayout.WEST, degreeSpecLabel, 10, SpringLayout.WEST, degreePanel);

		// degreeSpec
		springLayout.putConstraint(SpringLayout.NORTH, degreeSpec, -3, SpringLayout.NORTH, degreeSpecLabel);
		springLayout.putConstraint(SpringLayout.WEST, degreeSpec, 10, SpringLayout.EAST, degreeSpecLabel);

		// departmentLabel
		springLayout.putConstraint(SpringLayout.NORTH, departmentLabel, 10, SpringLayout.SOUTH, degreeSpecLabel);
		springLayout.putConstraint(SpringLayout.EAST, departmentLabel, 0, SpringLayout.EAST, degreeSpecLabel);

		// department
		springLayout.putConstraint(SpringLayout.NORTH, department, -3, SpringLayout.NORTH, departmentLabel);
		springLayout.putConstraint(SpringLayout.WEST, department, 10, SpringLayout.EAST, departmentLabel);
	}
	
	public void setTexts(String major, String spec, String department) {
		degreeMajor.setText(major);
		degreeSpec.setText(spec);
		this.department.setText(department);
	}
}
