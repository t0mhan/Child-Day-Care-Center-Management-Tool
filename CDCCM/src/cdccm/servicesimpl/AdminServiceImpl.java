package cdccm.servicesimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.utilities.CdccmUtilities;


public class AdminServiceImpl implements AdminService {
	
	private MySQLDBConnector dbConnector;
	
	public AdminServiceImpl(){
		dbConnector = MySQLDBConnector.getInstance();
	}
	@Override
	public void insertChildDetails(ChildPOJO childPOJO) throws SQLException{
		int age = CdccmUtilities.getAge();
	
		// on the basis of age_calculartor utility we can decide age group, no additional query to get fk_age_group
		int ageGroup = 2;
		int resultCountChild = dbConnector.
				insert("INSERT INTO CHILD_INFO(name,surname,dob,age,fk_age_group,fk_idparent) VALUES('" +childPOJO.getFirst_name()+ "','"
		        +childPOJO.getLast_name() + "','"+childPOJO.getDob()+ "','"+age+"','"+ageGroup+"',"+"(SELECT MAX(IDPARENT) from PARENT)"+")");
		
		if (resultCountChild  > 0)
			System.out.println("Child Record Inserted Successfully");
		else
			System.out.println("Error Inserting Record Please Try Again");
	}
	
	public void insertParentDetails(ParentPOJO parentPOJO, ContactPOJO contactPOJO) throws SQLException{
		
		int resultCountParent = dbConnector.
				insert("INSERT INTO PARENT(name, surname) VALUES('" 
				+parentPOJO.getParentFirst_name()+ "','"+parentPOJO.getParentLast_name()+"')");
		
		int resultCountContact = dbConnector.
				insert("INSERT INTO CONTACT(street,city,pincode,phone_number,emailid,fk_idparent) VALUES('" +contactPOJO.getStreet()+ "','"
		        +contactPOJO.getCity() + "','" + contactPOJO.getPincode() + "','" + contactPOJO.getPhoneNumber() + "','" + contactPOJO.getEmail()+"',"+"(SELECT MAX(IDPARENT) from PARENT)"+")");

		if ((resultCountParent > 0) && (resultCountContact > 0))
			System.out.println("Parent Record Inserted Successfully");
		else
			System.out.println("Error Inserting Record Please Try Again");
	}
	
	@Override
	public ResultSet listAllChild() throws SQLException {
		ResultSet childrenList;
		childrenList = dbConnector.query("SELECT * FROM CHILD_INFO");
		return childrenList;
	}
	@Override
	public void insertCareProvider(CareProviderPOJO careProviderPOJO) {

		int resultCountProvider;
		try {
			resultCountProvider = dbConnector.
					insert("INSERT INTO CARE_PROVIDER(name, emailid, phone_number) VALUES('" + careProviderPOJO.getName() + "','" + careProviderPOJO.getEmail() + "','" + careProviderPOJO.getPhoneNumber()+ "')");
			if ((resultCountProvider  > 0))
				System.out.println("Care Provider Record Inserted Successfully");
		else
				System.out.println("Error Inserting Record Please Try Again");
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	@Override
	public void generateReport(int childid) {
		System.out.println("gerenating report for Child " + childid);
		ResultSet childresult;
		String sql="select ci.idchild as childId,ci.name as Name,ci.surname as Surname,ci.dob as dateOfBirth, ag.name as ageGroup,a.activity_name  as activityName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as Total,((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentagefrom report r join child_info ci join day_session ds join age_group ag join activity aon(r.fk_idchild=ci.idchild and r.fk_idactivity=a.idactivity and ci.fk_age_group=ag.idage_group) where r.fk_idchild=? group by a.activity_name,ci.idchild;";
		ChildReportPOJO childreport=null;
		try {
			ResultSet resultset=dbConnector.getReport(sql,childid);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

}
