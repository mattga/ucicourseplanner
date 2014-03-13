package edu.uci.ics.mattg.sql;

import javax.swing.JTable;

public class QueryTable extends JTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueryTable () {
		
	}
	
	public QueryTable(QueryTableModel qtm) {
		this.setFocusable(true);
		this.setModel(qtm);
	}
	
	public void setModel (QueryTableModel qtm) {
		super.setModel(qtm);
	}
}
