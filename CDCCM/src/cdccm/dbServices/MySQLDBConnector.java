package cdccm.dbServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//import edu.sdp.dbServices.MySQLDBConnector;


public class MySQLDBConnector {
	public Connection conn;
	private Statement resultStatement;
	private static MySQLDBConnector dbConnectorObj;
	
	private MySQLDBConnector() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "cdccmDB";
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
	
	/* SQL queries to be written here
	 * insert update delete
	 */
	

}
