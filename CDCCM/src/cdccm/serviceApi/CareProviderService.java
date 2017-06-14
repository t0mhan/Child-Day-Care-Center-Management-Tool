package cdccm.serviceApi;

import java.sql.ResultSet;
import java.util.List;

import cdccm.pojo.AssignActivityPOJO;

public interface CareProviderService {
	void childPerformance(AssignActivityPOJO updatePerformance);
	List<AssignActivityPOJO> displayChild(int id);
}
