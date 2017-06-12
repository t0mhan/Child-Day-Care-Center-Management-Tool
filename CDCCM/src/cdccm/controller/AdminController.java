package cdccm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.servicesimpl.AdminServiceImpl;
import cdccm.utilities.CdccmUtilities;
import cdccm.utilities.EmailValidator;;

public class AdminController {

	private Scanner inputScanner;
	private boolean choiceFlag = true;
	private AdminService adminService;

	public AdminController(Scanner inputScanner) {
		this.inputScanner = inputScanner;
		adminService = new AdminServiceImpl();
	}

	public void startOperations() {
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
				AddParent();
				System.out.println("That's All Folks!! ");
				break;
			case 2:// done
				AddCareProvider();
				break;
			case 3:// done
				AddActivitiesToAllChildren();
				break;
			case 4:// done
				AddActivityToChild();
				break;
			case 5:// done
				UpdateRegistrationInfo();
				break;
			case 6:// done
				ShowAllChildren();
				break;
			case 7:// done
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
			case 11:// done
				ProvideFeedback();
				break;
			case 12:// done
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}

		} while (choiceFlag);
	}

	private void AddParent() {

		ParentPOJO parentPOJO = new ParentPOJO();

		List<ContactPOJO> contact = new ArrayList<>();

		System.out.println("++++++++++ Welcome To Parent Registration Portal ++++++++++\n");
		System.out.println("++++++++++ Please Enter Details Of Parent++++++++++\n ");
		System.out.println("Enter The First Name: ");
		parentPOJO.setParentFirst_name(inputScanner.nextLine());
		System.out.println("Enter The Last Name: ");
		parentPOJO.setParentLast_name(inputScanner.nextLine());

		boolean nextContact = true;
		do {// get multiple contacts if any
			ContactPOJO contactPOJO = new ContactPOJO();
			System.out.println("Enter The Street Name: ");
			contactPOJO.setStreet(inputScanner.nextLine());
			System.out.println("Enter The City: ");
			contactPOJO.setCity(inputScanner.nextLine());
			System.out.println("Enter The Pincode: ");
			contactPOJO.setPincode(Integer.parseInt(inputScanner.nextLine()));
			boolean emailflag = true;
			do {
				System.out.println("Enter The Email: ");
				String emailString = inputScanner.nextLine();
				EmailValidator email = new EmailValidator();
				boolean isEmail = (email.validate(emailString));
				if (isEmail == true) {
					contactPOJO.setEmail(emailString);
					emailflag = false;
				} else {
					System.out.println("Not Valid Email Id");
					emailflag = true;
				}
			} while (emailflag);
			System.out.println("Enter The Phone Number: ");
			contactPOJO.setPhoneNumber(inputScanner.nextLine());
			contact.add(contactPOJO);
			System.out.println("Want To Provide secondary Contact (Y/N)");
			String input = inputScanner.nextLine();
			if (input.equals("Y") || input.equals("y")) {
				nextContact = true;
			} else {
				nextContact = false;
			}
		} while (nextContact);
		// add list of contacts to parent object
		parentPOJO.setContact(contact);
		boolean isparentinserted = adminService.insertParentDetails(parentPOJO);
		if (isparentinserted) {
			System.out.println("Parent Info Inserted Successfuly....!!");
			this.AddChild(parentPOJO);
		} else {
			System.out.println("Parent Info NOT Inserted ");

		}
	}

	private void AddChild(ParentPOJO parentPOJO) {
		boolean nextchild = true;
		List<ChildPOJO> children = new ArrayList<>();
		do {// get multiple children if any
			ChildPOJO child = new ChildPOJO();
			System.out.println("++++++++++ Please Enter Details Of Child ++++++++++\n ");
			System.out.println("Enter The First Name: ");
			child.setFirst_name(inputScanner.nextLine());
			System.out.println("Enter The Last Name: ");
			child.setLast_name(inputScanner.nextLine());

			boolean dateflag = true;
			do {
				System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
				String date = inputScanner.nextLine();
				if (CdccmUtilities.isValidFormat(date)) {
					child.setDob(date);
					dateflag = false;
				}
			} while (dateflag);
			System.out.println("Do you have second child (Y/N)");
			String input = inputScanner.nextLine();

			children.add(child);// add child to list of children of this parent
			if (input.equals("Y") || input.equals("y")) {
				nextchild = true;
			} else {
				nextchild = false;
			}
		} while (nextchild);
		parentPOJO.setChild(children);// associate children to parent
		boolean isChildInserted = adminService.insertChildDetails(parentPOJO);
		if (isChildInserted) {
			System.out.println("Child-parent-contact  Inserted Successfully");
		} else {
			System.out.println("No Record Inserted");

		}

	}

	private void AddCareProvider() {

		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		System.out.println(
				"++++++++++ Welcome To Care Provider Registration Portal, Please Enter Details Of Care Provider ++++++++++\n");
		System.out.println("Enter The Complete Name: ");
		careProviderPOJO.setName(inputScanner.nextLine());
		boolean emailflag = true;
		do {
			System.out.println("Enter The Email: ");
			String emailString = inputScanner.nextLine();
			EmailValidator email = new EmailValidator();
			boolean isEmail = (email.validate(emailString));
			if (isEmail == true) {
				careProviderPOJO.setEmail(emailString);
				emailflag = false;
			} else {
				System.out.println("Not Valid Email Id");
				emailflag = true;
			}
		} while (emailflag);
		System.out.println("Enter The Phone Number: ");
		careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
		adminService.insertCareProvider(careProviderPOJO);
	}

	private void ShowAllChildren() {
		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void AddActivityToChild() {
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

	private void UpdateChildActivity() {
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		boolean moreEntry = true;

		System.out.println("\nEnter Child ID To Update Activity ");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
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

	private void AddActivitiesToAllChildren() {
		System.out.println("Welcome To Assiging All Children Acivities Based On Their Age Group");
		adminService.assignActivitiesToChildren();
		
	}

	private void UpdateRegistrationInfo() {

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

	private void UpdateCareProviderInfo() {

		CareProviderPOJO careProviderPOJO = new CareProviderPOJO();
		System.out.println("Enter Care Provider ID To Update Data");
		int careProviderID = (Integer.parseInt(inputScanner.nextLine()));
		boolean showProvider = adminService.displayCareProvider(careProviderID);
		if (showProvider) {
			System.out.println("Enter The Complete Name: ");
			careProviderPOJO.setName(inputScanner.nextLine());
			boolean emailflag = true;
			do {
				System.out.println("Enter The Email: ");
				String emailString = inputScanner.nextLine();
				EmailValidator email = new EmailValidator();
				boolean isEmail = (email.validate(emailString));
				if (isEmail == true) {
					careProviderPOJO.setEmail(emailString);
					emailflag = false;
				} else {
					System.out.println("Not Valid Email Id");
					emailflag = true;
				}
			} while (emailflag);
			System.out.println("Enter The Phone Number: ");
			careProviderPOJO.setPhoneNumber(inputScanner.nextLine());
			adminService.updateCareProviderInfo(careProviderID, careProviderPOJO);
		} else {
			System.out.println("No Record Found !!");
		}
	}

	private void UpdateParentInfo() {
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
			boolean emailflag = true;
			do {
				System.out.println("Enter The Email: ");
				String emailString = inputScanner.nextLine();
				EmailValidator email = new EmailValidator();
				boolean isEmail = (email.validate(emailString));
				if (isEmail == true) {
					contactPOJO.setEmail(emailString);
					emailflag = false;
				} else {
					System.out.println("Not Valid Email Id");
					emailflag = true;
				}
			} while (emailflag);
			adminService.updateParentInfo(parentID, parentPOJO);
			adminService.updateContactInfo(parentID, contactPOJO);
		} else {
			System.out.println("No Record Found !!");
		}
	}

	private void UpdateChildInfo() {
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
			boolean dateflag = true;
			do {
				System.out.println("Enter The Date Of Birth in format (yyyy-mm-dd): ");
				String date = inputScanner.nextLine();
				if (CdccmUtilities.isValidFormat(date)) {
					childPOJO.setDob(date);
					dateflag = false;
				}
			} while (dateflag);
			adminService.updateChildInfo(id, childPOJO);
		} else
			System.out.println("No Record Found !!");
	}

	private void GenerateReport() {

	}

	private void GenerateSchedule() {
		// TODO Auto-generated method stub

	}

	private void ProvideFeedback() {

		ProviderFeedbackPOJO providerFeedbackPOJO = new ProviderFeedbackPOJO();
		System.out.println("Enter Parent ID");
		providerFeedbackPOJO.setParentId(Integer.parseInt(inputScanner.nextLine()));
		System.out.println("Enter Care Provider ID To Add Feedback/Suggestions");
		providerFeedbackPOJO.setCareProviderId(Integer.parseInt(inputScanner.nextLine()));
		boolean careProviderExists = adminService.displayCareProvider(providerFeedbackPOJO.getCareProviderId());
		if (careProviderExists) {
			System.out.println("Please Enter Feedback/Suggestions For Care Provider");
			providerFeedbackPOJO.setFeedback(inputScanner.nextLine());
			adminService.provideFeedback(providerFeedbackPOJO);
		} else {
			System.out.println("No Record Found !!");
		}

	}

}
