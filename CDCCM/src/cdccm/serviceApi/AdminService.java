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

	void updateChildInfo(int id, ChildPOJO childPOJO) throws SQLException, ParseException;

	void updateParentInfo(int parentID, ParentPOJO parentPOJO) throws SQLException;

	void updateContactInfo(int parentID, ContactPOJO contactPOJO) throws SQLException;

	void updateCareProviderInfo(int careProviderID, CareProviderPOJO careProviderPOJO) throws SQLException;

	void selectReport();

	void selectSchedule();

	void selectNewsEvents();

	void generateReport(int childid);

	ResultSet listAllChild() throws SQLException;

	boolean displayInfo(int id, String tableName) throws SQLException;

	boolean displayChild(int id) throws SQLException;

	boolean displayParent(int id) throws SQLException;

	boolean displayContact(int id) throws SQLException;

	boolean displayCareProvider(int id) throws SQLException;

}
