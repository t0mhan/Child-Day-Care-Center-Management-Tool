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

	public void ProviderController(Scanner inputScanner) {
		this.inputScanner = inputScanner;
		providerService = new CareProviderImpl();
	}

	public void startOperations() throws SQLException, ParseException {
		do {
			System.out.println(
					"User Logged In As Care Provider \nNow Select An Operation To Perform \n1. Enter Performance OF Child \n2. Main Menu ");
			int choice = Integer.parseInt(inputScanner.nextLine());
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
	private void ChildPerformance(){
		AssignActivityPOJO assignActivityPOJO = new AssignActivityPOJO();
		System.out.println("Please Enter Child's ID To Give Permormance Details ");
		assignActivityPOJO.setChildID(inputScanner.nextInt());
		System.out.println("Select For Which Session You Want To Give Performance (Select 1 For Morning 2 For Afternoon 3 For Evening)");
		assignActivityPOJO.setSession(inputScanner.nextInt());
		
	}
}
