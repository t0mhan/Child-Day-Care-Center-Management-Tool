package cdccm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import org.jfree.chart.imagemap.ImageMapUtilities;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.servicesimpl.AdminServiceImpl;
import cdccm.utilities.EmailValidator;;

public class AdminController {

	private Scanner inputScanner;
	private boolean choiceFlag = true;
	private AdminService adminService;

	public AdminController(Scanner inputScanner) {
		this.inputScanner = inputScanner;
		adminService = new AdminServiceImpl();
	}

	public void startOperations() throws Exception {
		System.out.println("User Entered As Admin \n");
		do {
			System.out.println("Please Select An Operation To Perform");
			System.out.println(
					"1. Register A Child \n2. Register Care Provider \n3. Add Activity To All Children \n4. Add Activity For A Child \n5. Update Child, Prent or Care Provider Info \n6. List All Children   \n7. Update Activity and Care Provider "
							+ "\n8. Send News or Events To Parent \n9. Send Schedule Of Child\n10.Generate Performance Report Of Child \n11.Provide Feedback  Of Care Provider \n12.Main Menu.");
			int choice = 0;
			choice = Integer.parseInt(inputScanner.nextLine());
			switch (choice) {
			case 1:// done
				try {
					AddParent();
					AddChild();

					boolean addChildFlag = true;
					do {
						System.out.println("Want To Register Another Child (Y/N)");
						char input = inputScanner.next().charAt(0);
						if (input == 'Y' || input == 'y') {
							AddChild();
						} else {
							System.out.println("Register Another Child Option Not Selected Prpperly, Try Again");
							addChildFlag = false;
						}
					} while (addChildFlag);

					System.out.println("That's All Folks!! ");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:// done
				AddCareProvider();
				break;
			case 3:
				AddActivitiesToAllChildren();
				break;
			case 4://done
				AddActivityToChild();
				break;
			case 5://done
				UpdateRegistrationInfo();
				break;
			case 6:// done
				ShowAllChildren();
				break;
			case 7:
				UpdateChildActivity();
				break;
			case 8:
				GenerateSchedule();
				break;
			case 9:
				SendNewsEvents();
				break;
			case 10:
				GenerateReport();
				break;
			case 11://done
				ProvideFeedback();
				break;
			case 12:
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}

		} while (choiceFlag);
	}

	private void AddParent() throws SQLException {

		ParentPOJO parentPOJO = new ParentPOJO();
		ContactPOJO contactPOJO = new ContactPOJO();

		System.out.println("++++++++++ Welcome To Parent Registration Portal ++++++++++\n");
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
		contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
		System.out.println("Enter The Email: ");
		String emailString = inputScanner.nextLine();
		EmailValidator email = new EmailValidator();
		boolean isEmail = (email.validate(emailString));
		if (isEmail == true) {
			contactPOJO.setEmail(emailString);
		} else
			System.out.println("Not Valid Email Id");
		System.out.println("Enter The Phone Number: ");
		contactPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertParentDetails(parentPOJO, contactPOJO);
	}

	private void AddChild() throws SQLException, ParseException {

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

		System.out.println(
				"++++++++++ Welcome To Care Provider Registration Portal, Please Enter Details Of Care Provider ++++++++++\n");
		System.out.println("Enter The Complete Name: ");
		careProviderPOJO.setName(inputScanner.nextLine());
		System.out.println("Enter The Email: ");
		String emailString = inputScanner.nextLine();
		EmailValidator email = new EmailValidator();
		boolean isEmail = (email.validate(emailString));
		if (isEmail == true) {
			careProviderPOJO.setEmail(emailString);
		} else
			System.out.println("Not Valid Email Id");
		System.out.println("Enter The Phone Number: ");
		careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertCareProvider(careProviderPOJO);
	}

	private void ShowAllChildren() throws SQLException {
		System.out.println("Here Is List Of All Children");
		ResultSet childList = adminService.listAllChild();
		int numberOfChild = 1;

		while (childList.next()) {
			System.out.println("Details of Child: " + numberOfChild);
			System.out.println("First Name:" + childList.getString("name"));
			System.out.println("Last Name:" + childList.getString("surname"));
			System.out.println("Date Of Birth:" + childList.getString("dob"));
			System.out.println("Age:" + childList.getString("age"));
			numberOfChild++;
			System.out.println("---------");
		}
		System.out.printf("Above Is The List Of %d Children", numberOfChild);
		System.out.println("");
	}

	private void AddActivityToChild() throws SQLException {
		boolean moreEntry = true;
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		System.out.println("Enter Child ID To Assign Activity and Care Provider");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
		adminService.displayInfo(assignActivityPOJO.getChildID(), "CHILD_INFO");
		System.out.println("Enter Child's AgeGroup");
		assignActivityPOJO.setAgeGroup(inputScanner.nextInt());
		do {

			System.out.println("Enter The Session (For Morning 1, For Afternoon 2, For Evening 3)");
			assignActivityPOJO.setSession(inputScanner.nextInt());
			adminService.assignActivityToChild(assignActivityPOJO);

			System.out.println("Do You Want To Assign more Activity? Press Yes");
			String choice = inputScanner.nextLine().toUpperCase();
			if (choice != "YES") {
				moreEntry = false;
			}
		} while (moreEntry);
	}

	private void UpdateChildActivity() throws SQLException, ParseException {
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		boolean moreEntry = true;

		System.out.println("\nEnter Child ID To Update Activity ");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
		System.out.println("Enter Child's AgeGroup");
		assignActivityPOJO.setAgeGroup(inputScanner.nextInt());
		do {
			System.out.println(
					"Select For Which Session You Want To Update (Select 1 For Morning 2 For Afternoon 3 For Evening)");
			assignActivityPOJO.setSession(inputScanner.nextInt());
			adminService.updateActivityToChild(assignActivityPOJO);

			System.out.println("Do You Want To Update More Activity? Press Yes");
			String choice = inputScanner.nextLine().toUpperCase();
			if (choice == "YES") {

			} else {
				moreEntry = false;
			}
		} while (moreEntry);
	}

	private void SendNewsEvents() {
		// TODO Auto-generated method stub
	}

	private void AddActivitiesToAllChildren() throws SQLException {
		System.out.println("Welcome To Assiging All Children Acivities Based On Their Age Group");
		adminService.assignActivitiesToChildren();
	}

	private void UpdateRegistrationInfo() throws Exception {

		boolean choiceFlag = true;
		int choice = 0;
		System.out.println("Welcome To Update Registration Information");

		do {
			System.out.println(
					"\nNow Select An Operation To Perform\n1. Update Child Information \n2. Update Parent Information \n3. Update Care Provider Information \n4. Exit Update Process ");
			choice = Integer.parseInt(inputScanner.nextLine());
			switch (choice) {

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
		} while (choiceFlag);
	}

	private void UpdateCareProviderInfo() throws SQLException {
		
		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		System.out.println("Enter Care Provider ID To Update Data");
		int careProviderID = (Integer.parseInt(inputScanner.nextLine()));
		boolean showProvider = adminService.displayCareProvider(careProviderID);
		if (showProvider) {
			System.out.println("Enter The Complete Name: ");
			careProviderPOJO.setName(inputScanner.nextLine());
			System.out.println("Enter The Email: ");
			String emailString = inputScanner.nextLine();
			EmailValidator email = new EmailValidator();
			boolean isEmail = (email.validate(emailString));
			if (isEmail == true) {
				careProviderPOJO.setEmail(emailString);
			} else
				System.out.println("Not Valid Email Id");
			System.out.println("Enter The Phone Number: ");
			careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
			adminService.updateCareProviderInfo(careProviderID, careProviderPOJO);
		} else {
			System.out.println("No Record Found !!");
		}
	}

	private void UpdateParentInfo() throws SQLException {
		ParentPOJO parentPOJO = new ParentPOJO();
		ContactPOJO contactPOJO = new ContactPOJO();
		System.out.println("Enter Parent ID To Update Data");
		int parentID = (Integer.parseInt(inputScanner.nextLine()));
		boolean showParent = adminService.displayParent(parentID);
		adminService.displayContact(parentID);
		System.out.println("");
		if (showParent) {
			System.out.println("Enter The First Name: ");
			parentPOJO.setParentFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			parentPOJO.setParentLast_name(inputScanner.nextLine());
			System.out.println("Enter The Street Name: ");
			contactPOJO.setStreet(inputScanner.nextLine());
			System.out.println("Enter The City: ");
			contactPOJO.setCity(inputScanner.nextLine());
			System.out.println("Enter The Pincode: ");
			contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
			System.out.println("Enter The Phone Number: ");
			contactPOJO.setPhoneNumber(inputScanner.nextLine());
			System.out.println("Enter The Email: ");
			String emailString = inputScanner.nextLine();
			EmailValidator email = new EmailValidator();
			boolean isEmail = (email.validate(emailString));
			if (isEmail == true) {
				contactPOJO.setEmail(emailString);
			} else
				System.out.println("Not Valid Email Id");
			adminService.updateParentInfo(parentID, parentPOJO);
			adminService.updateContactInfo(parentID, contactPOJO);
		} else {
			System.out.println("No Record Found !!");
		}
	}

	private void UpdateChildInfo() throws SQLException, Exception {
		System.out.println("Enter Child ID To Update Data");
		int id = (Integer.parseInt(inputScanner.nextLine()));
		ChildPOJO childPOJO = new ChildPOJO();
		boolean showChild = adminService.displayChild(id);
		if (showChild) {
			System.out.println("++++++++++ To Update the Data Please Enter Details Of Child ++++++++++\n ");
			System.out.println("Enter The First Name: ");
			childPOJO.setFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			childPOJO.setLast_name(inputScanner.nextLine());
			System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
			childPOJO.setDob(inputScanner.nextLine());
			adminService.updateChildInfo(id, childPOJO);
		} else
			System.out.println("No Record Found !!");
	}

	private void GenerateReport() {

	}

	private void GenerateSchedule() {
		// TODO Auto-generated method stub

	}
	private void ProvideFeedback() throws SQLException{
		System.out.println("Enter Care Provider ID To Add Feedback/Suggestions");
		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		int careProviderId = (Integer.parseInt(inputScanner.nextLine()));
		boolean careProviderExists = adminService.displayCareProvider(careProviderId);
		if(careProviderExists){
			System.out.println("Please Enter Feedback/Suggestions For Care Provider");
			careProviderPOJO.setSuggestions(inputScanner.nextLine());
			adminService.provideFeedback(careProviderId,careProviderPOJO );
		}else{
			System.out.println("No Record Found !!");
		}
		
	}

}
