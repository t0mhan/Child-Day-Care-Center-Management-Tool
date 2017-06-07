package cdccm.servicesimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import cdccm.dbServices.MySQLDBConnector;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.serviceApi.CareProviderService;

public class CareProviderImpl implements CareProviderService {
	Scanner inputChoice = new Scanner(System.in);
	private MySQLDBConnector dbConnector;

	public CareProviderImpl() {
		dbConnector = MySQLDBConnector.getInstance();
	}

	@Override
	public void ChildPerformance(AssignActivityPOJO updatePerformance) throws SQLException, ParseException {

	}

	@Override
	public boolean displayChild(int id) throws SQLException {
		boolean recordExists = false;
		ResultSet resultSetParent = dbConnector.query("SELECT * FROM REPORT WHERE IDPARENT = " + id);

		if (resultSetParent.next()) {
			System.out.println("Parent First Name: " + resultSetParent.getString("name"));
			System.out.println("Parent Last Name: " + resultSetParent.getString("surname"));
			recordExists = true;
		}
		return recordExists;
	}

}
