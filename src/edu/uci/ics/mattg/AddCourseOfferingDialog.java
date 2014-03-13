package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.uci.ics.mattg.collections.ArrayMap;
import edu.uci.ics.mattg.collections.Map;
import edu.uci.ics.mattg.ui.MapSearchKeyListener;
import edu.uci.ics.mattg.ui.PanelButtonModel;

public class AddCourseOfferingDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final short INPUT_COMPONENT_PADDING = 5;
	private JComboBox<String> quarterComboBox;
	private JComboBox<Integer> yearComboBox;
	private JLabel label, quarterLabel, yearLabel, courseLabel, year, searchIcon;
	private JButton ok, cancel;
	private JPanel buttonPanel, inputPanel, bodyPanel, coursesPanel;
	private SpringLayout springLayout;
	private ButtonGroup quarter;
	private JTextField courseSearchField;
	private PanelButtonModel panelModel;
	private Map<String, CourseItem> courseMap;
	
	public AddCourseOfferingDialog(JDialog parentComponent, CourseQueryTableModel ctm, JLabel year, ButtonGroup quarter) {
		super(parentComponent, "Add Course Offering", true);
		
		this.year = year;
		this.quarter = quarter;
		this.courseMap = new ArrayMap<String,CourseItem>();
		
		String [] courses = new String[ProgramState.courseMap.size()];
		int i = 0;
		CourseItem newItem;
		for (CourseItem e : ProgramState.courseMap.values()) {
			courses[i++] = e.getText();
			String key = (e.getDepartment() + e.getNumber() + ":" + e.getTitle()).toLowerCase().replaceAll(" ", "");

			newItem = new CourseItem(e.getDepartment(), e.getNumber(), e.getTitle(), e.getUnits(), e.getDescription());
			courseMap.put(key, newItem);
		}
		
		quarterComboBox = new JComboBox<String>(new String[] {"Fall", "Winter", "Spring", "Summer 5 Week I", "Summer 5 Week II", "Summer 10 Week"});
		quarterComboBox.setSelectedItem(((CourseTableButtonModel)quarter.getSelection()).getText());
		
		Integer y = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		yearComboBox = new JComboBox<Integer>(new Integer[] {y, y+1, y+2, y+3, y+4});
		yearComboBox.setSelectedItem(year.getText());
		
		label = new JLabel("Select the course(s), quarter, and year of the projected course offering.");
		
		quarterLabel = new JLabel("Quarter:");
		
		yearLabel = new JLabel("Year:");
		
		courseLabel = new JLabel("Course(s):");

		searchIcon = new JLabel(createImageIcon("1380024235_search.png", ""));
		searchIcon.setSize(20,20);
		
		coursesPanel = new JPanel();
		coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
		
		panelModel = new PanelButtonModel(coursesPanel);
		panelModel.addAll(courseMap.values());
		
		courseSearchField = new JTextField(30);
		courseSearchField.addKeyListener(new MapSearchKeyListener(courseMap, courseSearchField, panelModel));
		
		JScrollPane scrollPane = new JScrollPane(coursesPanel);
		scrollPane.setPreferredSize(new Dimension(300, 400));
		
		springLayout = new SpringLayout();
		
		inputPanel = new JPanel(springLayout);
		inputPanel.add(quarterLabel);
		inputPanel.add(quarterComboBox);
		inputPanel.add(yearLabel);
		inputPanel.add(yearComboBox);
		inputPanel.add(courseLabel);
		inputPanel.add(courseSearchField);
		inputPanel.add(searchIcon);
		
		/** SpringLayout constraints */
		// quarterLabel
		springLayout.putConstraint(SpringLayout.EAST, quarterLabel, 0, SpringLayout.EAST, courseLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, quarterLabel, -INPUT_COMPONENT_PADDING, SpringLayout.SOUTH, quarterComboBox);
		
		// quarterComboBox
		springLayout.putConstraint(SpringLayout.WEST, quarterComboBox, INPUT_COMPONENT_PADDING, SpringLayout.EAST, quarterLabel);
		springLayout.putConstraint(SpringLayout.NORTH, quarterComboBox, INPUT_COMPONENT_PADDING*2, SpringLayout.NORTH, inputPanel);
		
		// yearLabel
		springLayout.putConstraint(SpringLayout.EAST, yearLabel, 0, SpringLayout.EAST, courseLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, yearLabel, -INPUT_COMPONENT_PADDING, SpringLayout.SOUTH, yearComboBox);
		
		// yearComboBox
		springLayout.putConstraint(SpringLayout.WEST, yearComboBox, INPUT_COMPONENT_PADDING, SpringLayout.EAST, yearLabel);
		springLayout.putConstraint(SpringLayout.NORTH, yearComboBox, INPUT_COMPONENT_PADDING, SpringLayout.SOUTH, quarterComboBox);
		
		// courseLabel
		springLayout.putConstraint(SpringLayout.WEST, courseLabel, INPUT_COMPONENT_PADDING, SpringLayout.WEST, inputPanel);
		springLayout.putConstraint(SpringLayout.NORTH, courseLabel, INPUT_COMPONENT_PADDING+6, SpringLayout.SOUTH, yearLabel);
		
		// searchIcon
		springLayout.putConstraint(SpringLayout.WEST, searchIcon, INPUT_COMPONENT_PADDING, SpringLayout.EAST, courseLabel);
		springLayout.putConstraint(SpringLayout.NORTH, searchIcon, INPUT_COMPONENT_PADDING, SpringLayout.SOUTH, yearComboBox);

		// courseSearchField
		springLayout.putConstraint(SpringLayout.WEST, courseSearchField, 0, SpringLayout.EAST, searchIcon);
		springLayout.putConstraint(SpringLayout.NORTH, courseSearchField, INPUT_COMPONENT_PADDING, SpringLayout.SOUTH, yearComboBox);
		
		bodyPanel = new JPanel(new GridLayout(2,1));
		bodyPanel.add(inputPanel);
		bodyPanel.add(scrollPane);
		
		ok = new JButton("Ok");
		ok.addActionListener(new AddCourseOfferingActionListener(this, quarterComboBox, yearComboBox, courseMap.values(), ctm));
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				AddCourseOfferingDialog.this.setVisible(false);
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		
		this.add(label, BorderLayout.NORTH);
		this.add(bodyPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setMinimumSize(new Dimension(450,325));
	}
	
	public void reset() {
		// reset check
		for(CourseItem c : courseMap.values())
			c.setSelected(false);
		
		// reset term
		yearComboBox.setSelectedItem(Integer.parseInt(year.getText()));
		quarterComboBox.setSelectedItem(((CourseTableButtonModel)quarter.getSelection()).getText());
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
}
