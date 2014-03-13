package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.collections.Map;
import edu.uci.ics.mattg.sql.QueryTableModel;
import edu.uci.ics.mattg.ui.MapSearchKeyListener;
import edu.uci.ics.mattg.ui.PanelButtonModel;
import edu.uci.ics.mattg.ui.RegexTextField;

public class AddCourseDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton ok, cancel;
	private JPanel buttonPanel, coursePanel, prereqsPanel;
	private JTextField courseTitle, courseNumber, department, courseCredits, prereqSearchField;
	private JLabel courseInfoLabel, courseTitleLabel, courseNumberLabel, departmentLabel, courseCreditsLabel, prereqsLabel, searchIcon;
	private PanelButtonModel panelModel;
	private JScrollPane scrollPane;
	private Map<String, CourseItem> prereqMap;

	public AddCourseDialog(JDialog parent, QueryTableModel qtm) {
		super(parent, "Add Course");

		prereqMap = new ArrayMap<String,CourseItem>();
		String [] courses = new String[ProgramState.courseMap.size()];
		int i = 0;
		CourseItem newItem;
		for (CourseItem e : ProgramState.courseMap.values()) {
			courses[i++] = e.getText();
			String key = (e.getDepartment() + e.getNumber() + ":" + e.getTitle()).toLowerCase().replaceAll(" ", "");

			newItem = new CourseItem(e.getDepartment(), e.getNumber(), e.getTitle(), e.getUnits(), e.getDescription());
			prereqMap.put(key, newItem);
		}
		
		prereqsLabel = new JLabel("<html><b>Prerequisites");
		
		searchIcon = new JLabel(createImageIcon("1380024235_search.png", ""));
		searchIcon.setSize(20,20);
		
		prereqsPanel = new JPanel();
		prereqsPanel.setLayout(new BoxLayout(prereqsPanel, BoxLayout.Y_AXIS));
		
		panelModel = new PanelButtonModel(prereqsPanel);
		panelModel.addAll(prereqMap.values());
		
		prereqSearchField = new JTextField(22);
		prereqSearchField.addKeyListener(new MapSearchKeyListener(prereqMap, prereqSearchField, panelModel));
		
		scrollPane = new JScrollPane(prereqsPanel);
		scrollPane.setPreferredSize(new Dimension(263, 250));
		
		courseInfoLabel = new JLabel("Course Information");
		courseInfoLabel.setFont(new Font(courseInfoLabel.getFont().getName(), Font.BOLD, 11));

		courseTitleLabel = new JLabel("Course Title:");

		courseTitle = new JTextField(20);

		courseNumberLabel = new JLabel("Course Number:");

		courseNumber = new RegexTextField(5, "[a-z,A-Z,0-9]", 4);

		departmentLabel = new JLabel("Department:");

		department = new RegexTextField(5, "[a-z,A-Z,0-9]", 8);

		courseCreditsLabel = new JLabel("Course Credits:");

		courseCredits = new RegexTextField(3, "[0-9]", 3);

		ok = new JButton("Ok");
		ok.addActionListener(new AddCourseActionListener(this, courseTitle, courseNumber, department, courseCredits, prereqMap.values(), qtm));
		
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
		coursePanel.add(prereqsLabel);
		coursePanel.add(searchIcon);
		coursePanel.add(prereqSearchField);
		coursePanel.add(scrollPane);

		configureSpringLayout(springLayout);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCourseDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		this.setMinimumSize( new Dimension(650, 400));
		this.setResizable(true);
		this.add(coursePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	void configureSpringLayout(SpringLayout springLayout) {

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
		
		// prereqsLabel
		springLayout.putConstraint(SpringLayout.NORTH, prereqsLabel, 0, SpringLayout.NORTH, courseInfoLabel);
		springLayout.putConstraint(SpringLayout.WEST, prereqsLabel, 10, SpringLayout.EAST, courseTitle);
		
		// searchIcon
		springLayout.putConstraint(SpringLayout.NORTH, searchIcon, 5, SpringLayout.SOUTH, prereqsLabel);
		springLayout.putConstraint(SpringLayout.WEST, searchIcon, 5, SpringLayout.EAST, courseTitle);

		// prereqSearchField
		springLayout.putConstraint(SpringLayout.NORTH, prereqSearchField, 0, SpringLayout.NORTH, searchIcon);
		springLayout.putConstraint(SpringLayout.WEST, prereqSearchField, 0, SpringLayout.EAST, searchIcon);

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
	
	public void addPrereq(CourseItem c) {
		String key = (c.getDepartment() + c.getNumber() + ":" + c.getTitle()).toLowerCase().replaceAll(" ", "");
		prereqMap.put(key, c);
		panelModel.addButton(c);
	}

	public void removePrereq(String key) {
		panelModel.removeButton(prereqMap.getValue(key));
		prereqMap.remove(key);
	}

	public void updatePrereq(String key) {
		prereqMap.remove(key);
	}
	
	public void clearPrereqSelection() {
		for (CourseItem c : prereqMap.values())
			c.setSelected(false);
	}
}
