package edu.uci.ics.mattg;

import javax.swing.JCheckBox;

public class DegreeItem extends JCheckBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String major, specialization, department;

	public DegreeItem(String major, String specialization, String department) {
		this.major = major;
		this.department = department;
		this.specialization = specialization;

		this.setFocusable(false);
		buildCheckBox();
	}

	public void buildCheckBox() {
		String s = "<b>" + major + (this.specialization.equals("") ? "" : ":</b> " + specialization);
		String html1 = "<html><body style='width: ";
		String html2 = "px'>";

		//		        courseDT.addWord(courseNumber + " " + courseTitle);
		//		        courseDT.addWord(courseNumber);
		//		        courseDT.addWord(courseTitle);
		//		        String[] cNSplit = courseNumber.split(" ");
		//		        for (String s1 : cNSplit)
		//		        	courseDT.addWord(s1);
		//		        String[] cTSplit = courseTitle.split(" ");
		//		        for (String s2 : cTSplit)
		//		        	courseDT.addWord(s2);
		//		        
		//		        courseDT.addWord(courseTitle);
		this.setText(html1+ProgramState.DEGREE_ITEM_WIDTH+html2+s);
	}

	public String getMajor() {
		return major;
	}
	
	public String getSpecialization() {
		return specialization;
	}
	
	public String toString() {
		return "CourseItem{" + major + "," + specialization + "," + isSelected() + "}";
	}
}