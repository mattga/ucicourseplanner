package edu.uci.ics.mattg.sql;

import javax.swing.table.AbstractTableModel;
import javax.naming.CommunicationException;

import edu.uci.ics.mattg.ProgramState;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

public class QueryTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int rowCount;
	protected Connection connection;
	protected Statement statement;
	protected ResultSet rs;
	protected ResultSetMetaData rsmd;
	
	public QueryTableModel (String url, String defaultQuery) throws SQLException, CommunicationException, ClassNotFoundException {
		Class.forName(ProgramState.jdbcDriver);
		connection = DriverManager.getConnection(url);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = statement.executeQuery(defaultQuery);
		rsmd = rs.getMetaData();
		rs.last();
		rowCount = rs.getRow();
	}

	public boolean executeQuery(String query) {
		try {
			rs = statement.executeQuery(query);
			rsmd = rs.getMetaData();
			rs.last();
			rowCount = rs.getRow();
		} catch (SQLException e) {
			rowCount = -1;
			return false;
		}
		this.fireTableStructureChanged();
		return true;
	}
	
	// Generic query
	public boolean execute(String query) throws SQLException {
		boolean result = statement.execute(query);
		if (result) {
			rs = statement.getResultSet();
			rsmd = rs.getMetaData();
			rs.last();
			rowCount = rs.getRow();
		}
		this.fireTableStructureChanged();
		return result;
	}
	
	// Table update delegated to caller
	public int executeUpdate(String query) {
		int result = -1;
		try {
			result = statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getColumnCount(){
		try {
			return rsmd.getColumnCount();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}
	
	// columnIndex for 0...N columns
	public String getColumnName(int columnIndex) {
		try {
			return rsmd.getColumnName(columnIndex+1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "NONAME";
	}

	public int getRowCount() {
		return rowCount;
	}

	// rowIndex/columnIndex for 0...N rows/columns
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			rs.absolute(rowIndex+1);  
			return rs.getObject(columnIndex+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	// Closes all connections
	public boolean disconnect() {
		try {
			rs.close();
			statement.close();
			connection.close();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public ResultSet getResultSet() {
		try {
			return statement.getResultSet();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
