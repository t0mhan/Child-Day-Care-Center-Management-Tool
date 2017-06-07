package cdccm.serviceApi;

import java.sql.SQLException;
import java.text.ParseException;

import cdccm.pojo.AssignActivityPOJO;

public interface CareProviderService {
	void ChildPerformance(AssignActivityPOJO updatePerformance) throws SQLException, ParseException;
	boolean displayChild(int id) throws SQLException;
}
