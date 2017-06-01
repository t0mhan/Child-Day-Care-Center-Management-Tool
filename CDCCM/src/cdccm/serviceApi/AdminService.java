package cdccm.serviceApi;

import java.sql.ResultSet;
import java.sql.SQLException;

import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;

public interface AdminService {
	void insertChildDetails(ChildPOJO childPOJO) throws SQLException;
	void insertParentDetails(ParentPOJO parentPOJO, ContactPOJO contactPOJO) throws SQLException;
	void insertCareProvider(CareProviderPOJO careProviderPOJO);
	void selectReport();
	void selectSchedule();
	void selectNewsEvents();
	ResultSet listAllChild() throws SQLException;

}
