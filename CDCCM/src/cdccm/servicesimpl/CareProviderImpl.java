package cdccm.servicesimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	public void childPerformance(AssignActivityPOJO updatePerformance) {
		int resultUpdate = 0;
		try {
			java.sql.PreparedStatement ps = dbConnector.getInstance().ps("UPDATE REPORT SET CARE_PROVIDER_FEEDBACK = ' "
					+ updatePerformance.getFeedback() + "' WHERE FK_IDCHILD = ? AND FK_IDSESSION = ?");
			ps.setInt(1, updatePerformance.getChildID());
			ps.setInt(2, updatePerformance.getSession());
			resultUpdate = ps.executeUpdate();
			if (resultUpdate > 0) {
				System.out.println("Child Feedback Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean displayChild(AssignActivityPOJO updatePerformance) {
		ResultSet resultSetReport = null;
		ResultSet resultSetActivity = null;
		boolean recordExists = false;
		try {
			java.sql.PreparedStatement ps = dbConnector.getInstance()
					.ps("SELECT fk_idsession,FK_IDACTIVITY  FROM REPORT WHERE fk_idchild = ? ");
			ps.setInt(1, updatePerformance.getChildID());
			resultSetReport = ps.executeQuery();
			while (resultSetReport.next()) {
				System.out.println("Session : " + resultSetReport.getString("fk_idsession"));
				System.out.println("Activity ID : " + resultSetReport.getString("FK_IDACTIVITY"));
				updatePerformance.setActivityID(resultSetReport.getInt("FK_IDACTIVITY"));
				java.sql.PreparedStatement selectActivity = dbConnector.getInstance()
						.ps("SELECT ACTIVITY_NAME, ACTIVITY_DESCRIPTION FROM ACTIVITY WHERE IDACTIVITY = ? ");
				selectActivity.setInt(1, updatePerformance.getActivityID());
				resultSetActivity = selectActivity.executeQuery();
				while (resultSetActivity.next()) {
					System.out.println("Activity Name: " + resultSetActivity.getString("ACTIVITY_NAME"));
					System.out.println("Activity Description: " + resultSetActivity.getString("ACTIVITY_DESCRIPTION"));
					System.out.println("");
				}
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}

}
