package cdccm.serviceApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;

public interface AdminService {
	void insertChildDetails(ChildPOJO childPOJO) throws SQLException, ParseException;
	void insertParentDetails(ParentPOJO parentPOJO, ContactPOJO contactPOJO) throws SQLException;
	void insertCareProvider(CareProviderPOJO careProviderPOJO);
	void assignActivityToChild(AssignActivityPOJO assignActivityPOJO) throws SQLException;
	void updateActivityToChild(AssignActivityPOJO assignActivityPOJO) throws SQLException;
	void assignActivitiesToChildren() throws SQLException;
	void updateChildInfo(int id, ChildPOJO childPOJO) throws SQLException;
	void updateParentInfo(int parentID);
	void updateCareProviderInfo(int careProviderID);
	void selectReport();
	void selectSchedule();
	void selectNewsEvents();
	void generateReport(int childid);
	ResultSet listAllChild() throws SQLException;
	ResultSet displayInfo(int id,String tableName) throws SQLException;

}
