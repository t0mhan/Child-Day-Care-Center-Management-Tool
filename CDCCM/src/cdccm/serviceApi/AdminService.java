package cdccm.serviceApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;

public interface AdminService {

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

	void generateReport(int childid) throws SQLException;

	void GenerateScheduleReport() throws SQLException;

	List<ChildPOJO> listAllChild();

	void generateBulckPerformanceReport();

	ChildPOJO displayChild(int id);

	ParentPOJO displayParent(int id);

	CareProviderPOJO displayCareProvider(int id);

	void insertMealDetails(List<FoodPOJO> foodlist);

	void deleteMealDay(FoodPOJO foodPOJO);

	void updateFood(FoodPOJO foodPOJO);

	boolean insertChildDetails(ParentPOJO parentpojo) throws SQLException, ParseException;

	void dumpReportToArchive();

}
