package cdccm.dbServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.expression.spel.ast.Assign;

import com.lowagie.text.List;

import cdccm.helper.PropertyReader;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentNamePlate;
import cdccm.pojo.ParentPOJO;

public class MySQLDBConnector {
	public Connection conn = null;
	private Statement resultStatement = null;
	private static MySQLDBConnector dbConnectorObj = null;
	private PreparedStatement preparedstatement = null;
	PropertyReader directory = null;

	private MySQLDBConnector() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "child_care";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "admin";
		this.directory = new PropertyReader();
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

	public ArrayList<ParentNamePlate> getParentNameplate(String query) throws SQLException {
		ResultSet resultset = null;
		ArrayList<ParentNamePlate> parentnameplate = new ArrayList<ParentNamePlate>();
		try {
			resultStatement = conn.createStatement();
			resultset = resultStatement.executeQuery(query);
			while (resultset.next()) {
				parentnameplate
						.add(new ParentNamePlate(resultset.getString(1), resultset.getString(2), resultset.getInt(3)));
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

	public ResultSet assignActivity(String query, int age, int ageGruop) {
		ResultSet resultSetActivity = null;
		try {
			preparedstatement = conn.prepareStatement(query);
			preparedstatement.setInt(1, age);
			preparedstatement.setInt(2, ageGruop);
			resultSetActivity = preparedstatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSetActivity;

	}

	public int updateChild(String query, ChildPOJO childObj, int ageGroup, int childId) {
		int resultUpdate = 0;
		try {
			preparedstatement = conn.prepareStatement(query);
			preparedstatement.setString(1, childObj.getFirst_name());
			preparedstatement.setString(2, childObj.getLast_name());
			preparedstatement.setString(3, childObj.getDob());
			preparedstatement.setInt(4, childObj.getAge());
			preparedstatement.setInt(5, ageGroup);
			preparedstatement.setInt(6, childId);
			resultUpdate = preparedstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultUpdate;
	}
	
	public int updateParent(String query, ParentPOJO parentObj, int id) {
		int resultUpdate = 0;
		try {
			preparedstatement = conn.prepareStatement(query);
			preparedstatement.setString(1, parentObj.getParentFirst_name());
			preparedstatement.setString(2, parentObj.getParentLast_name());
			preparedstatement.setInt(3, id);
			resultUpdate = preparedstatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultUpdate;
	}

		public int updateContact(String query, ContactPOJO contactObj, int id) {
			int resultUpdate = 0;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setString(1, contactObj.getStreet());
				preparedstatement.setString(2, contactObj.getCity());
				preparedstatement.setInt(3, contactObj.getPincode());
				preparedstatement.setString(4, contactObj.getPhoneNumber());
				preparedstatement.setString(5, contactObj.getEmail());
				preparedstatement.setInt(6, id);
				resultUpdate = preparedstatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultUpdate;

	}
		
		public int updateCareProvider(String query, CareProviderPOJO providerObj, int id) {
			int resultUpdate = 0;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setString(1, providerObj.getName());
				preparedstatement.setString(2, providerObj.getEmail());
				preparedstatement.setString(3, providerObj.getPhoneNumber());
				preparedstatement.setInt(4, id);
				resultUpdate = preparedstatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultUpdate;
	}
		public ResultSet displayInfo(String query, int id) {
			ResultSet resultSetActivity = null;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setInt(1, id);
				resultSetActivity = preparedstatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultSetActivity;
	}
		public ResultSet selectActivity(String query, AssignActivityPOJO assignActivityObj) {
			ResultSet resultSetActivity = null;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setInt(1, assignActivityObj.getAgeGroup());
				preparedstatement.setInt(2, assignActivityObj.getSession());
				resultSetActivity = preparedstatement.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultSetActivity;
	}
		
		public int updateReport(String query, AssignActivityPOJO activityObj) {
			int resultUpdate = 0;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setInt(1, activityObj.getActivityID());
				preparedstatement.setInt(2, activityObj.getCareProviderID());
				preparedstatement.setInt(3, activityObj.getChildID());
				preparedstatement.setInt(4, activityObj.getSession());
				resultUpdate = preparedstatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultUpdate;
	}
		public int giveFeedback(String query, AssignActivityPOJO activityObj) {
			int resultUpdate = 0;
			try {
				preparedstatement = conn.prepareStatement(query);
				preparedstatement.setString(1, activityObj.getFeedback());
				preparedstatement.setInt(2, activityObj.getChildID());
				preparedstatement.setInt(3, activityObj.getSession());
				
				resultUpdate = preparedstatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultUpdate;
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
