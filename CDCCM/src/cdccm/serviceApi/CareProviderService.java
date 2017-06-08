package cdccm.serviceApi;

import java.sql.SQLException;
import java.text.ParseException;

import cdccm.pojo.AssignActivityPOJO;

public interface CareProviderService {
	void childPerformance(AssignActivityPOJO updatePerformance) throws SQLException, ParseException;
	boolean displayChild(AssignActivityPOJO updatePerformance) throws SQLException;
}
