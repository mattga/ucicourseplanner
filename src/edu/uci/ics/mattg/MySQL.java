package edu.uci.ics.mattg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL {
	private static Connection conn;
	private static MySQL instance;

	public MySQL() {
		try {
			Class.forName(ProgramState.jdbcDriver);
			conn = DriverManager.getConnection(ProgramState.url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static MySQL getInstance() {
		if(instance == null)
			instance = new MySQL();
		return instance;
	}
	
	public static Connection getConnection() {
		return conn;
	}
	
	public static Statement getStatement() throws SQLException {
		return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
}
