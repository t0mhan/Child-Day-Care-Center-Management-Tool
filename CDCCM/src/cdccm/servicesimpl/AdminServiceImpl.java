package cdccm.servicesimpl;

import cdccm.dbServices.MySQLDBConnector;
import cdccm.services.AdminService;
//import edu.sdp.dbServices.MySQLDBConnector;

public class AdminServiceImpl implements AdminService {
	
	private MySQLDBConnector dbConnector;
	
	public AdminServiceImpl(){
		dbConnector = MySQLDBConnector.getInstance();
	}
	public void addChild(){
		
	}

}
