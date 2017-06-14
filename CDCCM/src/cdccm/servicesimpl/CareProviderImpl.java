package cdccm.servicesimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cdccm.dbServices.MySQLDBConnector;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.serviceApi.CareProviderService;
import net.sf.jasperreports.ant.UpdaterElement;

public class CareProviderImpl implements CareProviderService {
	Scanner inputChoice = new Scanner(System.in);
	private MySQLDBConnector dbConnector;

	public CareProviderImpl() {
		dbConnector = MySQLDBConnector.getInstance();
	}
	@Override
	public void childPerformance(AssignActivityPOJO updatePerformance) {
		int resultUpdate = 0;
		String feedbackQuery = "UPDATE REPORT SET CARE_PROVIDER_FEEDBACK = ? WHERE FK_IDCHILD = ? AND FK_IDSESSION = ?";
		resultUpdate = dbConnector.giveFeedback(feedbackQuery, updatePerformance);
		if (resultUpdate > 0) {
			System.out.println("Child Feedback Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");
	}

	@Override
	public List<AssignActivityPOJO> displayChild(int id) {
		AssignActivityPOJO activityObj = null;
		List<AssignActivityPOJO> listOfChildActivity=new ArrayList<>();
		ResultSet resultSetReport = null;
		ResultSet resultSetActivity = null;
		String activityQuery = "SELECT fk_idsession,FK_IDACTIVITY  FROM REPORT WHERE fk_idchild = ? ";
		String availableActivityQuery = "SELECT ACTIVITY_NAME, ACTIVITY_DESCRIPTION FROM ACTIVITY WHERE IDACTIVITY = ? ";
		try {
			resultSetReport = dbConnector.displayInfo(activityQuery, id);
			while (resultSetReport.next()) {
				//Add list of assignactivityPOJO (we might have 3 activity for single child)
				activityObj=new AssignActivityPOJO();
				activityObj.setSession(resultSetReport.getInt("fk_idsession"));
				activityObj.setActivityID(resultSetReport.getInt("FK_IDACTIVITY"));
				resultSetActivity = dbConnector.displayInfo(availableActivityQuery, id);
				if (resultSetActivity.next()) {
					
					activityObj.setActivityName(resultSetActivity.getString("ACTIVITY_NAME"));
					activityObj.setDescription(resultSetActivity.getString("ACTIVITY_DESCRIPTION"));
					
				}
				listOfChildActivity.add(activityObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listOfChildActivity;
	}

}
