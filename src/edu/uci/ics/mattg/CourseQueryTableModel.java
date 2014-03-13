package edu.uci.ics.mattg;

import java.sql.SQLException;

import javax.naming.CommunicationException;

import edu.uci.ics.mattg.sql.QueryTableModel;

public class CourseQueryTableModel extends QueryTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseQueryTableModel(String url, String defaultQuery) throws CommunicationException, ClassNotFoundException, SQLException {
		super(url, defaultQuery);
	}
	
	public String getColumnName(int columnIndex) {
		try {
			if(columnIndex == rsmd.getColumnCount())
				return "";
			return getNameForAttribute(super.rsmd.getColumnName(columnIndex+1));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "NONAME";
	}
	 
	private String getNameForAttribute(String name) {
		switch(name) {
		case "department":
			return "Department";
		case "course_number":
			return "Course Number";
		case "course_title":
			return "Course Title";
		case "group_concat(s.department,' ',s.course_number ORDER BY s.course_number)":
			return "Selection Set";
		case "selection_set_req":
			return "Amount Required";
		}
		
		return name;
	}
}
