package edu.uci.ics.mattg;

import edu.uci.ics.mattg.ui.SelectableButtonModel;

public class CourseTableButtonModel extends SelectableButtonModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CourseQueryTableModel ctm;
	private String text;
	private boolean isSchedule;

	public CourseTableButtonModel(CourseQueryTableModel ctm, String text, boolean isSchedule) {
		CourseTableButtonModel.ctm = ctm;
		this.text = text;
		this.isSchedule = isSchedule;
	}

	@Override
	public void setPressed(boolean b) {
		super.setPressed(b);
		if(isSchedule) {
			ProgramState.scheduleQuarter = this.text;
			ctm.executeQuery(ProgramState.getScheduleQuery());
		} else {
			ProgramState.projectedCourseQuarter = this.text;
			ctm.executeQuery(ProgramState.getProjectedCoursesQuery());
		}
	}

	public String getText() {
		return text;
	}
}
