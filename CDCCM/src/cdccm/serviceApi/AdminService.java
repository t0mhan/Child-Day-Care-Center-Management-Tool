package cdccm.serviceApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;

public interface AdminService {
	boolean insertChildDetails(ParentPOJO parentchild);

	boolean insertParentDetails(ParentPOJO parentPOJO);

	void insertCareProvider(CareProviderPOJO careProviderPOJO);

	void assignActivityToChild(AssignActivityPOJO assignActivityPOJO);

	void updateActivityToChild(AssignActivityPOJO assignActivityPOJO);

	void assignActivitiesToChildren();

	void updateChildInfo(int id, ChildPOJO childPOJO);

	void updateParentInfo(int parentID, ParentPOJO parentPOJO);

	void updateContactInfo(int parentID, ContactPOJO contactPOJO);

	void updateCareProviderInfo(int careProviderID, CareProviderPOJO careProviderPOJO);

	void provideFeedback(ProviderFeedbackPOJO providerFeedbackPOJO);

	void selectReport();

	void selectSchedule();

	void selectNewsEvents();

	void generateReport(int childid);

	ResultSet listAllChild() throws SQLException;

	boolean displayInfo(int id, String tableName);

	boolean displayChild(int id);

	boolean displayParent(int id);

	boolean displayCareProvider(int id);

}
