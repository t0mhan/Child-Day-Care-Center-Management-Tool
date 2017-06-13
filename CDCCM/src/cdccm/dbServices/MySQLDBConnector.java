package cdccm.dbServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.lowagie.text.List;

import cdccm.helper.PropertyReader;
import cdccm.pojo.ParentNamePlate;

public class MySQLDBConnector {
	public Connection conn=null;
	private Statement resultStatement=null;
	private static MySQLDBConnector dbConnectorObj=null;
	private PreparedStatement preparedstatement=null;
	PropertyReader directory=null;
	
	private MySQLDBConnector() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "child_care";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "admin";
        this.directory=new PropertyReader();
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
	public ArrayList<ParentNamePlate> getParentNameplate(String query) throws SQLException
	{   ResultSet resultset=null;
	    ArrayList<ParentNamePlate> parentnameplate=new ArrayList<ParentNamePlate>();
		try {
			resultStatement = conn.createStatement();
			resultset = resultStatement.executeQuery(query);
			while(resultset.next())
			{
				parentnameplate.add(new ParentNamePlate(resultset.getString(1),resultset.getString(2),resultset.getInt(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return parentnameplate;
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
		ResultSet resultset = null;

		preparedstatement = conn.prepareStatement(sql);
		preparedstatement.setInt(1, childid);

		return preparedstatement.executeQuery();
	}
	
	public PreparedStatement ps (String query){
		try {
			preparedstatement=conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return preparedstatement;
		
	}

	public ResultSet callProcedure(String sql, int childid, int groupid) {
		ResultSet resultset = null;
		int flag = 0;

		CallableStatement callablestatement;
		try {
			callablestatement = conn.prepareCall(sql);
			callablestatement.setInt(1, childid);
			callablestatement.setInt(2, groupid);

			flag = callablestatement.executeUpdate();
			callablestatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (flag > 0) {
			System.out.println("Procedure Executed Successfully");
			try {
				preparedstatement = conn.prepareStatement("select * from timetable");
				resultset = preparedstatement.executeQuery();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		} else {
			System.out.println("Procedure NOT Executed Successfully");
		}

		return resultset;
	}
}
