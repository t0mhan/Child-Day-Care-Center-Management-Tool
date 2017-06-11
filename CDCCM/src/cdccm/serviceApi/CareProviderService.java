package cdccm.serviceApi;

import cdccm.pojo.AssignActivityPOJO;

public interface CareProviderService {
	void childPerformance(AssignActivityPOJO updatePerformance);
	boolean displayChild(AssignActivityPOJO updatePerformance);
}
