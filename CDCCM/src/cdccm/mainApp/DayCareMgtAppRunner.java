package cdccm.mainApp;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import org.springframework.beans.CachedIntrospectionResults;

import cdccm.controller.AdminController;
import cdccm.controller.CareProviderController;

public class DayCareMgtAppRunner {
	public static void main(String[] args) throws Exception {
		boolean choiceFlag = true;
		Scanner inputChoice = new Scanner(System.in);
		System.out.println("++++++++++ Welcome To Day Care Center Management Application +++++++++");
		do {
			System.out.print(
					"\nPlease Selcet A User Type\n1. Admin \n2. Care Provider \n3. Exit \nPlease Enter Your choice: \n");
			int choice = 0;
			choice = Integer.parseInt(inputChoice.nextLine());
			switch (choice) {
			case 1:
				AdminController adminCtrl = new AdminController(inputChoice);
				adminCtrl.startOperations();
				break;
			case 2:
				CareProviderController careCtrl = new CareProviderController();
				careCtrl.startOperations();
				break;
			case 3:
				choiceFlag = false;
				break;
			default:
				System.out.println("Please Enter Above Mentioned Option Only, Let's Try Again!!");
				break;
			}

		} while (choiceFlag);
		inputChoice.close();
		System.out.println("Thank You, We Are Done For The Day!");
	}

}
