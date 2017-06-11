package cdccm.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import cdccm.pojo.AssignActivityPOJO;
import cdccm.serviceApi.CareProviderService;
import cdccm.servicesimpl.CareProviderImpl;

public class CareProviderController {
	private Scanner inputScanner;
	private boolean choiceFlag = true;
	private CareProviderService providerService;

	public CareProviderController(Scanner inputScanner) {
		this.inputScanner = inputScanner;
		providerService = new CareProviderImpl();
	}

	public void startOperations() throws SQLException, ParseException {
		System.out.println("User Entered In As Care Provider \n");
		do {

			System.out.println("Now Select An Operation To Perform \n1. Enter Performance OF Child \n2. Main Menu ");
			int choice = 0;
			choice = Integer.parseInt(inputScanner.nextLine());
			switch (choice) {
			case 1:
				ChildPerformance();
				break;
			case 2:
				choiceFlag = false;
				break;
			default:
				System.out.println("OOPS, You Have Entered Wrong Choice!!\n Please Try Again!!");
				break;
			}
		} while (choiceFlag);
	}

	private void ChildPerformance() throws SQLException, ParseException {
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		System.out.println("Please Enter Child's ID To Give Permormance Details ");
		assignActivityPOJO.setChildID(Integer.parseInt(inputScanner.nextLine()));
		boolean showChild = providerService.displayChild(assignActivityPOJO);
		if (showChild) {
			System.out.println(
					"Select For Which Session You Want To Give Performance (Select 1 For Morning 2 For Afternoon 3 For Evening)");
			assignActivityPOJO.setSession(Integer.parseInt(inputScanner.nextLine()));
			System.out.println("Please Provide Areas Where Child is Good And Areas Where More Focus Is Needed!!");
			assignActivityPOJO.setFeedback(inputScanner.nextLine());
			providerService.childPerformance(assignActivityPOJO);
		} else {
			System.out.println("Record Not Found In Report Table!!");
		}

	}
}
