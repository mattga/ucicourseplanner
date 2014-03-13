package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.collections.Map;
import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.sql.QueryTableModel;
import edu.uci.ics.mattg.ui.MapSearchKeyListener;
import edu.uci.ics.mattg.ui.PanelButtonModel;
import edu.uci.ics.mattg.ui.RegexTextField;

public class UpdateDegreeRequirementDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton ok, cancel;
	private JPanel buttonPanel, dReqPanel, coursesPanel;
	private JTextField dReqAmountField, courseSearchField;
	private JLabel dReqInfoLabel, degreeLabel, dReqAmountLabel, searchIcon, selSetLabel;
	private PanelButtonModel panelModel;
	private JComboBox<String> degreeComboBox;
	private JScrollPane scrollPane;
	private Map<String, CourseItem> courseMap;
	private String[] degrees;
	private CourseItem newItem;

	public UpdateDegreeRequirementDialog(JDialog parent, QueryTable qt, QueryTableModel qtm, QueryTableModel ssQtm) {
		super(parent, "Update Degree Requirement");

		int i = 0;
		degrees = new String[ProgramState.degreeMap.size()];
		for (DegreeItem s : ProgramState.degreeMap.values())
			degrees[i++] = s.getText();

		degreeComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(degrees));

		courseMap = new ArrayMap<String,CourseItem>();
		i = 0;
		for (CourseItem e : ProgramState.courseMap.values()) {
			String key = (e.getDepartment() + e.getNumber() + ":" + e.getTitle()).toLowerCase().replaceAll(" ", "");

			newItem = new CourseItem(e.getDepartment(), e.getNumber(), e.getTitle(), e.getUnits(), e.getDescription());
			courseMap.put(key, newItem);
		}

		searchIcon = new JLabel(createImageIcon("1380024235_search.png", ""));
		searchIcon.setSize(20,20);

		coursesPanel = new JPanel();
		coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));

		panelModel = new PanelButtonModel(coursesPanel);
		panelModel.addAll(courseMap.values());

		courseSearchField = new JTextField(25);
		courseSearchField.addKeyListener(new MapSearchKeyListener(courseMap, courseSearchField, panelModel));

		scrollPane = new JScrollPane(coursesPanel);
		scrollPane.setPreferredSize(new Dimension(288, 250));

		dReqInfoLabel = new JLabel("Requirement Info");
		dReqInfoLabel.setFont(new Font(dReqInfoLabel.getFont().getName(), Font.BOLD, 11));

		degreeLabel = new JLabel("Degree:");

		dReqAmountLabel = new JLabel("Required Amount:");

		dReqAmountField = new RegexTextField(4, "[0-9]", 2);

		selSetLabel = new JLabel("Selection Set:");

		ok = new JButton("Ok");
				ok.addActionListener(new UpdateDegreeRequirementActionListener(this, degreeComboBox, dReqAmountField, courseMap.values(), qtm, ssQtm));

		SpringLayout springLayout = new SpringLayout();
		dReqPanel = new JPanel(springLayout);
		dReqPanel.add(dReqInfoLabel);
		dReqPanel.add(degreeLabel);
		dReqPanel.add(degreeComboBox);
		dReqPanel.add(dReqAmountField);
		dReqPanel.add(dReqAmountLabel);
		dReqPanel.add(selSetLabel);
		dReqPanel.add(searchIcon);
		dReqPanel.add(courseSearchField);
		dReqPanel.add(scrollPane);
		configureSpringLayout(springLayout);

		cancel = new JButton("Cancel");	
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateDegreeRequirementDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		this.setMinimumSize(new Dimension(430, 420));
		this.setResizable(true);
		this.add(dReqPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	void configureSpringLayout(SpringLayout springLayout) {

		/** SprimgLayout Constraints **/
		// courseInfoLabel
		springLayout.putConstraint(SpringLayout.NORTH, dReqInfoLabel, 10, SpringLayout.NORTH, dReqPanel);
		springLayout.putConstraint(SpringLayout.WEST, dReqInfoLabel, 0, SpringLayout.WEST, degreeLabel);

		// degreeLabel
		springLayout.putConstraint(SpringLayout.NORTH, degreeLabel, 10, SpringLayout.SOUTH, dReqInfoLabel);
		springLayout.putConstraint(SpringLayout.EAST, degreeLabel, 0, SpringLayout.EAST, dReqAmountLabel);

		// degreeComboBox
		springLayout.putConstraint(SpringLayout.NORTH, degreeComboBox, -3, SpringLayout.NORTH, degreeLabel);
		springLayout.putConstraint(SpringLayout.WEST, degreeComboBox, 10, SpringLayout.EAST, degreeLabel);

		// dReqAmountLabel
		springLayout.putConstraint(SpringLayout.NORTH, dReqAmountLabel, 10, SpringLayout.SOUTH, degreeLabel);
		springLayout.putConstraint(SpringLayout.WEST, dReqAmountLabel, 10, SpringLayout.WEST, dReqPanel);

		// dReqAmountField
		springLayout.putConstraint(SpringLayout.NORTH, dReqAmountField, -3, SpringLayout.NORTH, dReqAmountLabel);
		springLayout.putConstraint(SpringLayout.WEST, dReqAmountField, 10, SpringLayout.EAST, dReqAmountLabel);

		// selSetLabel
		springLayout.putConstraint(SpringLayout.NORTH, selSetLabel, 10, SpringLayout.SOUTH, dReqAmountLabel);
		springLayout.putConstraint(SpringLayout.EAST, selSetLabel, 00, SpringLayout.EAST, dReqAmountLabel);

		// searchIcon
		springLayout.putConstraint(SpringLayout.NORTH, searchIcon, -3, SpringLayout.NORTH, selSetLabel);
		springLayout.putConstraint(SpringLayout.WEST, searchIcon, 0, SpringLayout.WEST, dReqAmountField);

		// courseSearchField
		springLayout.putConstraint(SpringLayout.NORTH, courseSearchField, 0, SpringLayout.NORTH, searchIcon);
		springLayout.putConstraint(SpringLayout.WEST, courseSearchField, 0, SpringLayout.EAST, searchIcon);

		// scrollPane
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 4, SpringLayout.SOUTH, searchIcon);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, searchIcon);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
			String description) {
		java.net.URL imgURL = getClass().getClassLoader().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void setTexts(String degree, int amount, String set) {
		degreeComboBox.setSelectedItem(degree);
		dReqAmountField.setText("" + amount);

		String [] sSet = set.split(",");
		String [][] courses = new String[sSet.length][2];
		for(int i = 0; i < sSet.length; i++) {
			courses[i][0] = sSet[i].split(" ")[0];
			courses[i][1] = sSet[i].split(" ")[1];
		}

		for (CourseItem e : courseMap.values()) {
			e.setSelected(false);
			for(int i = 0; i < courses.length; i++) {
				if(e.getDepartment().equals(courses[i][0]) && e.getNumber().equals(courses[i][1])){
					System.out.println(e.getDepartment() + "==" + courses[i][0] + ";" + e.getNumber() + "==" + courses[i][1]);
					e.setSelected(true);
				}
			}
		}

		panelModel.clear();
		panelModel.addAll(courseMap.values());
	}
}
