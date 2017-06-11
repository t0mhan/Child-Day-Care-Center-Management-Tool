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
	public void childPerformance(AssignActivityPOJO updatePerformance) throws SQLException, ParseException {
		int resultUpdate = 0;
		String updateQuery = "UPDATE REPORT SET CARE_PROVIDER_FEEDBACK = ' " + updatePerformance.getFeedback()
				+ "' WHERE FK_IDCHILD = " + updatePerformance.getChildID() + " AND FK_IDSESSION = "
				+ updatePerformance.getSession();
		resultUpdate = dbConnector.insert(updateQuery);
		if (resultUpdate > 0) {
			System.out.println("Child Feedback Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");
	}

	@Override
	public boolean displayChild(AssignActivityPOJO updatePerformance) throws SQLException {
		boolean recordExists = false;
		ResultSet resultSetParent = dbConnector
				.query("SELECT * FROM REPORT WHERE fk_idchild = " + updatePerformance.getChildID());

		while (resultSetParent.next()) {
			System.out.println("Session : " + resultSetParent.getString("fk_idsession"));
			System.out.println("Activity ID : " + resultSetParent.getString("FK_IDACTIVITY"));
			updatePerformance.setActivityID(resultSetParent.getInt("FK_IDACTIVITY"));
			ResultSet activityResuult = dbConnector
					.query("SELECT ACTIVITY_NAME, ACTIVITY_DESCRIPTION FROM ACTIVITY WHERE IDACTIVITY = "
							+ updatePerformance.getActivityID());
			while (activityResuult.next()) {
				System.out.println("Activity Name: " + activityResuult.getString("ACTIVITY_NAME"));
				System.out.println("Activity Description: " + activityResuult.getString("ACTIVITY_DESCRIPTION"));
				System.out.println("");
			}
			recordExists = true;
		}
		return recordExists;
	}

}
