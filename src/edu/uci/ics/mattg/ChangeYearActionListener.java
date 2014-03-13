package edu.uci.ics.mattg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class ChangeYearActionListener implements ActionListener {
	private boolean inc;
	private int minYear, maxYear;
	private JLabel label;
	private static CourseQueryTableModel ctm;
	private boolean isSchedule;

	public ChangeYearActionListener(boolean inc, JLabel label, int minYear, int maxYear, CourseQueryTableModel ctm, boolean isSchedule) {
		this.inc = inc;
		this.label = label;
		this.minYear = minYear;
		this.maxYear = maxYear;
		this.isSchedule = isSchedule;
		ChangeYearActionListener.ctm = ctm;
	}

	// TODO: Enumerate quarter in db
	public void actionPerformed (ActionEvent e) {
		int year = Integer.parseInt(label.getText());
		year = (inc ? (year + 1 > maxYear ? year : year + 1) : (year - 1 < minYear ? year : year - 1));
		label.setText("" + year);
		if(isSchedule) {
			ProgramState.scheduleYear = year;
			ctm.executeQuery(ProgramState.getScheduleQuery());
		} else {
			ProgramState.projectedCoursesYear = year;

			ctm.executeQuery(ProgramState.getProjectedCoursesQuery());
		}
	}
}
