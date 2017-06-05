package cdccm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.servicesimpl.AdminServiceImpl;;

public class AdminController {
	
	private Scanner inputScanner;
	private boolean choiceFlag = true;
	private AdminService adminService;
	
	public AdminController(Scanner inputScanner){
		this.inputScanner = inputScanner;
		adminService = new AdminServiceImpl();
	}
	
	public void startOperations() throws SQLException{
		do{
			System.out.println("User Logged In As Admin. \nNow Select An Operation To Perform");
			System.out.println("1. Register A Child \n2. Register Care Provider \n3.Register Activity To All Children \n4.Register Activity For A Single Child \n5.Update Child, Prent or Care Provider Info \n6.List All Children   \n7. Generate Performance Report Of Child "
					+ "\n8. Send News or Events To Parent \n9. Send Schedule Of Child   \n11. Main Menu.");
			int choice = 0;
			choice = Integer.parseInt(inputScanner.nextLine());
			switch(choice){
			case 1://done
				try {
					AddParent();
					AddChild();
				
					boolean addChildFlag = true;
					do{
						System.out.println("Want To Register Another Child (Y/N)");
						char input = inputScanner.next().charAt(0);
						if(input == 'Y' || input == 'y'){
							AddChild();
						}
						else{
							System.out.println("Register Another Child Option Not Selected Prpperly, Try Again");
							addChildFlag = false;
						}
					}while(addChildFlag);
						
					System.out.println("That's All Folks!! ");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2://done
				AddCareProvider();
				break;
			case 3:
				AddActivitiesToAllChildren();
				break;
			case 4:
				AddActivityToChild();
				break;
			case 5:
				UpdateRegistrationInfo();
				break;
			case 6://done
				ShowAllChildren();
				break;
			case 7:
				GenerateReport();
				break;
			case 8:
				GenerateSchedule();
				break;
			case 9:
				SendNewsEvents();
				break;
			case 10:
				
				break;
			case 11:
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}
			
		}while(choiceFlag);
	}
	
	private void AddParent() throws SQLException{
		
		ParentPOJO parentPOJO = new ParentPOJO();
		ContactPOJO contactPOJO = new ContactPOJO();
		
		System.out.println("++++++++++ Welcome To Parent Registration Portal, Please Enter Details Of Child ++++++++++\n");
		System.out.println("++++++++++ Please Enter Details Of Parent++++++++++\n ");
		System.out.println("Enter The First Name: ");
		parentPOJO.setParentFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		parentPOJO.setParentLast_name(inputScanner.nextLine());
		System.out.println("Enter The Street Name: ");
		contactPOJO.setStreet(inputScanner.nextLine());
		System.out.println("Enter The City: ");
		contactPOJO.setCity(inputScanner.nextLine());
		System.out.println("Enter The Pincode: ");
		contactPOJO.setPincode(inputScanner.nextInt());
		System.out.println("Enter The Email: ");
		contactPOJO.setEmail(inputScanner.nextLine());
		System.out.println("Enter The Phone Number: ");
		contactPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertParentDetails(parentPOJO,contactPOJO);
	}
		
	private void AddChild() throws SQLException {

		ChildPOJO childPOJO = new ChildPOJO();

		System.out.println("++++++++++ Please Enter Details Of Child ++++++++++\n ");
		System.out.println("Enter The First Name: ");
		childPOJO.setFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		childPOJO.setLast_name(inputScanner.nextLine());
		System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
		childPOJO.setDob(inputScanner.nextLine());
		adminService.insertChildDetails(childPOJO);
		
	}
	
	private void AddCareProvider() {
		
		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		
		System.out.println("++++++++++ Welcome To Care Provider Registration Portal, Please Enter Details Of Care Provider ++++++++++\n");
		System.out.println("Enter The Complete Name: ");
		careProviderPOJO.setName(inputScanner.nextLine());
		System.out.println("Enter The Email: ");
		careProviderPOJO.setEmail(inputScanner.nextLine());
		System.out.println("Enter The Phone Number: ");
		careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertCareProvider(careProviderPOJO);
	}
	
	private void ShowAllChildren() throws SQLException {
		System.out.println("Here Is List Of All Children");
		ResultSet childList = adminService.listAllChild();
		int numberOfChild = 1;
		
		while(childList.next()){
			System.out.println("Details of Child: "+numberOfChild);
			System.out.println("First Name:"+ childList.getString("name"));
			System.out.println("Last Name:"+ childList.getString("surname"));
			System.out.println("Date Of Birth:"+ childList.getString("dob"));
			System.out.println("Age:"+ childList.getString("age"));
			numberOfChild++;
			System.out.println("---------");
		}
		System.out.printf("Above Is The List Of %d Children",numberOfChild);
		System.out.println("");
	}

	private void SendNewsEvents() {
		// TODO Auto-generated method stub
	}

	
	private void AddActivityToChild(){
		
	}
	
	private void AddActivitiesToAllChildren() throws SQLException{
		System.out.println("Welcome To Assiging All Children Acivities Based On Their Age Group");
		adminService.assignActivitiesToChildren();
	}
	
	private void UpdateRegistrationInfo() throws SQLException{
		
		boolean choiceFlag = true;
		//String tableName="";
		int choice = 0;
		System.out.println("Welcome To Update Registration Information");
		System.out.println("Now Select An Operation To Perform\n1. Update Child Information \n2. Update Parent Information \n3. Update Care Provider Information \n4. Exit Update Process ");
		choice = Integer.parseInt(inputScanner.nextLine());
		do{
			switch(choice){
			case 1:
				UpdateChildInfo();
				break;
			case 2:
				UpdateParentInfo();
				break;
			case 3:
				UpdateCareProviderInfo();
				break;
			case 4:
				choiceFlag = false;
				break;
			default:
				System.out.println("Wrong Choice");
			}
		}while(choiceFlag);
	}
	
	
	private void UpdateCareProviderInfo() throws SQLException {
		System.out.println("Enter Care Provider ID To Update Data");
		int id = inputScanner.nextInt();
        
		
	}

	private void UpdateParentInfo() throws SQLException {
		System.out.println("Enter Care Provider ID To Update Data");
		int id = inputScanner.nextInt();
		ResultSet result = adminService.displayInfo(id, "PARENT");
	}

	private void UpdateChildInfo() throws SQLException {
		System.out.println("Enter Child ID To Update Data");
		int id = inputScanner.nextInt();
		ChildPOJO childPOJO = new ChildPOJO();
		ResultSet result = adminService.displayInfo(id, "CHILD_INFO");
		//System.out.println("Details of Child: "+result);
		System.out.println("++++++++++ To Update the Data Please Enter Details Of Child ++++++++++\n ");
		System.out.println("Enter The First Name: ");
		childPOJO.setFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		childPOJO.setLast_name(inputScanner.nextLine());
		System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
		childPOJO.setDob(inputScanner.nextLine());
		adminService.updateChildInfo(id, childPOJO);
	}


	private void GenerateReport() {
		
		
	}
	
	private void GenerateSchedule() {
		// TODO Auto-generated method stub
		
	}

}
