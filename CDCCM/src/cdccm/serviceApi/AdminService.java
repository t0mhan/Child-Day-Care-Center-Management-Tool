package cdccm.serviceApi;

import java.sql.SQLException;

import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ParentPOJO;

public interface AdminService {
	void insertChildDetails(ChildPOJO childPOJO, ParentPOJO parentPOJO) throws SQLException;
	void listAllChild();
	void insertCareProvider();
	void selectReport();
	void selectSchedule();
	void selectNewsEvents();

}
