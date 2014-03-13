package edu.uci.ics.mattg;

import javax.swing.JCheckBox;

public class CourseItem extends JCheckBox {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String courseTitle, courseNumber, courseDepartment, courseDescription;
		private int courseUnits;

		public CourseItem( String cDept, String cNum, String cTitle, int cUnits, String cDesc) {
			this.courseNumber = cNum;
			this.courseTitle = cTitle;
			this.courseDepartment = cDept;
			this.courseDescription = cDesc;
			this.courseUnits = cUnits;

			this.setFocusable(false);
			buildCheckBox();
		}

		public void buildCheckBox() {
			String s = "<b>" + courseDepartment + " " + courseNumber + ":</b> " + courseTitle;
			String html1 = "<html><body style='width: ";
	        String html2 = "px'>";

	        // TODO: Digital key implementation with adding keys to queue and separate thread to traverse DT
	        // TODO: Filter out and/the/etc and CS from courseNumber in case no space delimiter
//	        courseDT.addWord(courseNumber + " " + courseTitle);
//	        courseDT.addWord(courseNumber);
//	        courseDT.addWord(courseTitle);
//	        String[] cNSplit = courseNumber.split(" ");
//	        for (String s1 : cNSplit)
//	        	courseDT.addWord(s1);
//	        String[] cTSplit = courseTitle.split(" ");
//	        for (String s2 : cTSplit)
//	        	courseDT.addWord(s2);
//	        
//	        courseDT.addWord(courseTitle);
			this.setText(html1+ProgramState.COURSE_ITEM_WIDTH+html2+s);
		}
		
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			
			if(obj instanceof CourseItem) {
				if(((CourseItem)obj).courseDepartment.equals(this.courseDepartment) 
						&& ((CourseItem)obj).courseNumber.equals(this.courseNumber))
					return true;
			}
			return false;
		}
		
		public String toString() {
			return "CourseItem{" + courseNumber + "," + courseTitle + "," + isSelected() + "}";
		}
		
		public String getNumber() {
			return courseNumber;
		}
		
		public String getDepartment() {
			return courseDepartment;
		}
		
		public String getTitle() {
			return courseTitle;
		}
		
		public int getUnits() {
			return courseUnits;
		}
		
		public String getDescription() {
			return courseDescription;
		}
	}