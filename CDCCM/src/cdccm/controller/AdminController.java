package cdccm.controller;
import java.sql.SQLException;
import java.util.Scanner;

import cdccm.pojo.ChildPOJO;
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
	
	public void startOperations(){
		do{
			System.out.println("User Logged In As Admin. \n Now Select An Operation To Perform");
			System.out.println("1. Register A Child. 2. Register Care Provider \n 3. List All Children \n 4. Generate Performance Report Of Child "
					+ "\n 6. Send News or Events To Parent \n 7. Send Schedule Of Child   \n . Main Menu.");
			int choice = 0;
			choice = Integer.parseInt(inputScanner.nextLine());
			switch(choice){
			case 1:
				try {
					addChild();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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
	
	private void addChild() throws SQLException {
		
		ChildPOJO childPOJO = new ChildPOJO();
		ParentPOJO parentPOJO = new ParentPOJO();
		
		System.out.println("Please Enter Details Of Child: ");
		System.out.println("Enter The First Name: ");
		childPOJO.setFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		childPOJO.setLast_name(inputScanner.nextLine());
		System.out.println("Enter The Date Of Birth: ");
		childPOJO.setDob(inputScanner.nextLine());
		
		System.out.println("Please Enter Details Of Parent: ");
		System.out.println("Enter The First Name: ");
		parentPOJO.setParentFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		parentPOJO.setParentLast_name(inputScanner.nextLine());
		System.out.println("Enter The Street Name: ");
		parentPOJO.setStreet(inputScanner.nextLine());
		System.out.println("Enter The City: ");
		parentPOJO.setCity(inputScanner.nextLine());
		System.out.println("Enter The Pincode: ");
		parentPOJO.setPincode(inputScanner.nextInt());
		System.out.println("Enter The Phone Number: ");
		parentPOJO.setPhonenumber(inputScanner.nextLine());
		System.out.println("Enter The Email: ");
		parentPOJO.setEmail(inputScanner.nextLine());
		System.out.println("That's All Folks!! ");
		
		adminService.insertChildDetails(childPOJO, parentPOJO);
	}
	
	private void addCareProvider() {
		// TODO Auto-generated method stub
		
	}
	private void showAllChildren() {
		System.out.println("Here Is List Of All Children");
		adminService.listAllChild();
		
	}

	private void sendNewsEvents() {
		// TODO Auto-generated method stub
		
	}

	private void generateReport() {
		// TODO Auto-generated method stub
		
	}
	
	private void generateSchedule() {
		// TODO Auto-generated method stub
		
	}

	

	



	
	

}
