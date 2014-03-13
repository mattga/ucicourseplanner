package edu.uci.ics.mattg;

import edu.uci.ics.mattg.collections.List;
import edu.uci.ics.mattg.collections.Map;

public class ProgramState {
	public static final String jdbcDriver = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://localhost/courseplanner?user=courseplanner&password=tiger";
	public static final int COURSE_ITEM_WIDTH = 200;
	public static final int DEGREE_ITEM_WIDTH = 200;
	public static final int UNIT_LIMIT = 20;

	public static String projectedCourseQuarter;
	public static int projectedCoursesYear;
	public static String scheduleQuarter;
	public static int scheduleYear;
	public static boolean coursesUpdated = false;
	public static boolean degreesUpdated = false;
	public static boolean degreesUpdated2 = false;
	public static Map<String,CourseItem> courseMap;
	public static Map<String,DegreeItem> degreeMap;
	public static List<SelectionSetItem> selectionSetList;
	public static List<String> courseSchedule;

	public static String getProjectedCoursesQuery() {
		return "SELECT department, course_number, course_title FROM course_offerings NATURAL JOIN courses WHERE quarter='" + projectedCourseQuarter + "' AND year='" + projectedCoursesYear + "'";
	}
	
	public static String getScheduleQuery() {
		return "SELECT s.year, s.quarter, c.* FROM schedule AS s, courses AS c WHERE s.course_id=c.course_id AND s.quarter='" + scheduleQuarter + "' AND s.year='" + scheduleYear + "'";
	}
}
