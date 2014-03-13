package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.uci.ics.mattg.sql.QueryTable;
import edu.uci.ics.mattg.ui.NakedButton;

public class CourseScheduleDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel projectedCoursesLabel, selectedYear;
	private JPanel termSelectionPanel, quarterSelectionPanel, yearSelectionPanel, buttonPanel;
	private JButton inc_year, dec_year, winterQ, springQ, summer5wk1, summer5wk2, summer10wk, fallQ, done;
	private QueryTable qt; 
	private CourseQueryTableModel ctm;
	private ButtonGroup quarterButtonGroup;

	public CourseScheduleDialog(JDialog parent, String startQuarter, String endQuarter, int startYear, int endYear) {
		super(parent, true);

		String dep, num, term;
		try {
			MySQL.getInstance();
			Statement stmt = MySQL.getStatement();
			stmt.execute("DELETE FROM schedule");
			
			for(String s : ProgramState.courseSchedule.elements()) {
				String[] ss = s.split("_");
				dep = ss[1];
				num = ss[2];
				term = ss[3];
				
//				System.out.println("INSERT INTO schedule SELECT '"+ term.substring(0, term.length()-4) + "','" + term.substring(term.length()-4) + "',course_id FROM courses WHERE department='" + dep + "' AND course_number='" + num + "'");
				int res = stmt.executeUpdate("INSERT INTO schedule SELECT '"+ term.substring(0, term.length()-4) + "','" + term.substring(term.length()-4) + "',course_id FROM courses WHERE department='" + dep + "' AND course_number='" + num + "'");
				if(res == 0)
					System.out.println("Failed to insert course " + dep + num + " for " + term + " term.");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		projectedCoursesLabel = new JLabel("Select the year and quarter above to display the planned courses for that term.");
		
		ProgramState.scheduleQuarter = startQuarter;
		ProgramState.scheduleYear = startYear;
		selectedYear = new JLabel("" + startYear);

		try {
			ctm = new CourseQueryTableModel(ProgramState.url, ProgramState.getScheduleQuery());
			qt = new QueryTable(ctm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		dec_year = new NakedButton(createImageIcon("1380794953_back-alt.png", ""));
		dec_year.setPressedIcon(createImageIcon("1380794953_back-alt_pressed.png", ""));
		dec_year.addActionListener(new ChangeYearActionListener(false, selectedYear, startYear, endYear, ctm, true));

		inc_year = new NakedButton(createImageIcon("1380794953_forward-alt.png", ""));
		inc_year.setPressedIcon(createImageIcon("1380794953_forward-alt_pressed.png", ""));
		inc_year.addActionListener(new ChangeYearActionListener(true, selectedYear, startYear, endYear, ctm, true));

		yearSelectionPanel = new JPanel();
		yearSelectionPanel.add(dec_year);
		yearSelectionPanel.add(selectedYear);
		yearSelectionPanel.add(inc_year);

		winterQ = new NakedButton("Winter");
		winterQ.setModel(new CourseTableButtonModel(ctm, "Winter", true));
		if(ProgramState.scheduleQuarter.equals("Winter"))
			winterQ.setSelected(true);


		springQ = new NakedButton("Spring");
		springQ.setModel(new CourseTableButtonModel(ctm, "Spring", true));
		if(ProgramState.scheduleQuarter.equals("Spring"))
			springQ.setSelected(true);

		fallQ = new NakedButton("Fall");
		fallQ.setModel(new CourseTableButtonModel(ctm, "Fall", true));
		if(ProgramState.scheduleQuarter.equals("Fall"))
			fallQ.setSelected(true);

		summer5wk1 = new NakedButton("Summer 5 Week I");
		summer5wk1.setModel(new CourseTableButtonModel(ctm, "Summer 5 Week I", true));
		if(ProgramState.scheduleQuarter.equals("Summer5wkI"))
			summer5wk1.setSelected(true);


		summer5wk2 = new NakedButton("Summer 5 Week II");
		summer5wk2.setModel(new CourseTableButtonModel(ctm, "Summer 5 Week II", true));
		if(ProgramState.scheduleQuarter.equals("Summer5wkII"))
			summer5wk2.setSelected(true);


		summer10wk = new NakedButton("Summer 10 Week");
		summer10wk.setModel(new CourseTableButtonModel(ctm, "Summer 10 Week", true));
		if(ProgramState.scheduleQuarter.equals("Summer10wk"))
			summer10wk.setSelected(true);


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

		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseScheduleDialog.this.setVisible(false);
			}
		});

		buttonPanel = new JPanel();
		buttonPanel.add(done);

		this.add(termSelectionPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(qt), BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setTitle("Course Plan");
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
}
