package cdccm.servicesimpl;

import java.sql.SQLException;

import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.utilities.CdccmUtilities;


public class AdminServiceImpl implements AdminService {
	
	private MySQLDBConnector dbConnector;
	
	public AdminServiceImpl(){
		dbConnector = MySQLDBConnector.getInstance();
	}
	@Override
	public void insertChildDetails(ChildPOJO childPOJO, ParentPOJO parentPOJO) throws SQLException{
		int age = CdccmUtilities.getAge();
		
		int resultCountChild = dbConnector.
				insert("INSERT INTO CHILD(fname,lname,dob,age) VALUES('" +childPOJO.getFirst_name()+ "','"
		        +childPOJO.getLast_name() + "','"+childPOJO.getDob()+ "','"+age+"')");
		
		int resultCountParent = dbConnector.
				insert("INSERT INTO PARENT(fname,lname,street,city,pincode,phonenumber,email) VALUES('" 
				+parentPOJO.getParentFirst_name()+ "','"+parentPOJO.getParentLast_name() + "','"+parentPOJO.getStreet()+ "','"
				+parentPOJO.getCity()+ "','"+parentPOJO.getPincode()+ "','"+parentPOJO.getPhonenumber()+ "','"+parentPOJO.getEmail()+"')");
		
		
		if ((resultCountChild  > 0) && (resultCountParent > 0))
			System.out.println("Record Inserted Successfully");
		else
			System.out.println("Error Inserting Record Please Try Again");
	}
	@Override
	public void listAllChild() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void insertCareProvider() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectReport() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectSchedule() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectNewsEvents() {
		// TODO Auto-generated method stub
		
	}

}
