package cdccm.dbServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDBConnector {
	public Connection conn;
	private Statement resultStatement;
	private static MySQLDBConnector dbConnectorObj;
	
	private MySQLDBConnector() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "child_care";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";//default user name
		String password = "admin";//default password
		
		try {
			Class.forName(driver).newInstance();
			this.conn = (Connection) DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}
	}
	
	public static MySQLDBConnector getInstance() {
		if (dbConnectorObj == null)
			dbConnectorObj = new MySQLDBConnector();
		return dbConnectorObj;
	}
	
	public ResultSet query(String query) throws SQLException {
		resultStatement = conn.createStatement();
		ResultSet res = resultStatement.executeQuery(query);
		return res;
	}

	public int insert(String insertQuery) throws SQLException {
		resultStatement = conn.createStatement();
		int resultCount = resultStatement.executeUpdate(insertQuery);
		return resultCount;
	}
	
	public int delete(String deleteQuery) throws SQLException {
		resultStatement = conn.createStatement();
		int resultCount = resultStatement.executeUpdate(deleteQuery);
		return resultCount;
	}
	public ResultSet getReport(String sql, int childid) throws SQLException {
		preparedstatement=conn.prepareStatement(sql);
		preparedstatement.setInt(1,childid);
		ResultSet res=preparedstatement.executeQuery();
		return res;

}
