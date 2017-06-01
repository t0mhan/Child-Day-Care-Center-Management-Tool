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
			System.out.println("1. Register A Child. \n2. Register Care Provider \n3. List All Children \n4. Generate Performance Report Of Child "
					+ "\n5. Send News or Events To Parent \n6. Send Schedule Of Child   \n7. Main Menu.");
			int choice = 0;
			choice = Integer.parseInt(inputScanner.nextLine());
			switch(choice){
			case 1:
				try {
					addParent();
					addChild();
				
					boolean addChildFlag = true;
					do{
						System.out.println("Want To Register Another Child (Y/N)");
						char input = inputScanner.next().charAt(0);
						if(input == 'Y' || input == 'y'){
							addChild();
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
			case 2:
				addCareProvider();
				break;
			case 3:
				showAllChildren();
				break;
			case 4:
				generateReport();
				break;
			case 5:
				sendNewsEvents();
				break;
			case 6:
				generateSchedule();
				break;
			case 7:
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}
			
		}while(choiceFlag);
	}
	
	private void addParent() throws SQLException{
		
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
		
	private void addChild() throws SQLException {

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
	
	private void addCareProvider() {
		
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
	
	private void showAllChildren() throws SQLException {
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
		System.out.println("Above Is The List Of Children\n");
	}

	private void sendNewsEvents() {
		// TODO Auto-generated method stub
		
	}

	private void generateReport() {
		
		
	}
	
	private void generateSchedule() {
		// TODO Auto-generated method stub
		
	}

}
