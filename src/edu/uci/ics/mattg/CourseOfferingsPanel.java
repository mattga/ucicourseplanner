package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.ui.NakedButton;

public class CourseOfferingsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel projectedCoursesLabel, selectedYear;
	private JPanel termSelectionPanel, quarterSelectionPanel, yearSelectionPanel, buttonPanel;
	private JButton inc_year, dec_year, winterQ, springQ, summer5wk1, summer5wk2, summer10wk, fallQ, addCourse, removeCourse;
	private QueryTable qt; 
	private CourseQueryTableModel ctm;
	private ButtonGroup quarterButtonGroup;
	private AddCourseOfferingDialog addCourseOfferingDialog;
	private JScrollPane courseOfferingsSP;
	
	public CourseOfferingsPanel(JDialog parent) {
		
		projectedCoursesLabel = new JLabel("Select the year and quarter above to display the projected course offerings for that term.");
		
		ProgramState.projectedCourseQuarter = "Fall";
		int year = Calendar.getInstance().get(Calendar.YEAR);
		ProgramState.projectedCoursesYear = year;
		selectedYear = new JLabel("" + year);
		
		try {
			ctm = new CourseQueryTableModel(ProgramState.url, ProgramState.getProjectedCoursesQuery());
			qt = new QueryTable(ctm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		dec_year = new NakedButton(createImageIcon("1380794953_back-alt.png", ""));
		dec_year.setPressedIcon(createImageIcon("1380794953_back-alt_pressed.png", ""));
		dec_year.addActionListener(new ChangeYearActionListener(false, selectedYear, year, year+20, ctm, false));

		inc_year = new NakedButton(createImageIcon("1380794953_forward-alt.png", ""));
		inc_year.setPressedIcon(createImageIcon("1380794953_forward-alt_pressed.png", ""));
		inc_year.addActionListener(new ChangeYearActionListener(true, selectedYear, year, year+20, ctm, false));

		yearSelectionPanel = new JPanel();
		yearSelectionPanel.add(dec_year);
		yearSelectionPanel.add(selectedYear);
		yearSelectionPanel.add(inc_year);
		
		winterQ = new NakedButton("Winter");
		winterQ.setModel(new CourseTableButtonModel(ctm, "Winter", false));
		
		springQ = new NakedButton("Spring");
		springQ.setModel(new CourseTableButtonModel(ctm, "Spring", false));

		fallQ = new NakedButton("Fall");
		fallQ.setModel(new CourseTableButtonModel(ctm, "Fall", false));
		fallQ.setSelected(true);
		
		summer5wk1 = new NakedButton("Summer 5 Week I");
		summer5wk1.setModel(new CourseTableButtonModel(ctm, "Summer 5 Week I", false));
		
		summer5wk2 = new NakedButton("Summer 5 Week II");
		summer5wk2.setModel(new CourseTableButtonModel(ctm, "Summer 5 Week II", false));

		summer10wk = new NakedButton("Summer 10 Week");
		summer10wk.setModel(new CourseTableButtonModel(ctm, "Summer 10 Week", false));
		
		quarterButtonGroup = new ButtonGroup();
		quarterButtonGroup.add(fallQ);
		quarterButtonGroup.add(winterQ);
		quarterButtonGroup.add(springQ);
		quarterButtonGroup.add(summer5wk1);
		quarterButtonGroup.add(summer5wk2);
		quarterButtonGroup.add(summer10wk);
		
		quarterSelectionPanel = new JPanel();
		quarterSelectionPanel.add(fallQ);
		quarterSelectionPanel.add(winterQ);
		quarterSelectionPanel.add(springQ);
		quarterSelectionPanel.add(summer5wk1);
		quarterSelectionPanel.add(summer5wk2);
		quarterSelectionPanel.add(summer10wk);
		
		termSelectionPanel = new JPanel(new GridLayout(3, 1));
		termSelectionPanel.add(yearSelectionPanel);
		termSelectionPanel.add(quarterSelectionPanel);
		termSelectionPanel.add(projectedCoursesLabel);
		
		addCourseOfferingDialog = new AddCourseOfferingDialog(parent, ctm, selectedYear, quarterButtonGroup);
		
		addCourse = new JButton("Add Course Offering");
		addCourse.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				addCourseOfferingDialog.reset();
				addCourseOfferingDialog.setVisible(true);
			}
		});
		
		removeCourse = new JButton("Remove Course Offering");
		removeCourse.addActionListener(new DeleteCourseOfferingActionListener(this, qt, ctm, selectedYear, quarterButtonGroup));
		
		buttonPanel = new JPanel();
		buttonPanel.add(addCourse);
		buttonPanel.add(removeCourse);

		courseOfferingsSP = new JScrollPane(qt);

		this.setLayout(new BorderLayout());
		this.add(termSelectionPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(courseOfferingsSP, BorderLayout.CENTER);
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
}
