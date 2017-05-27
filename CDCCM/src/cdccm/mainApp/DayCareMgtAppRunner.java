package cdccm.mainApp;
import java.util.Scanner;

import cdccm.controller.AdminController;
import cdccm.controller.CareProviderController;
public class DayCareMgtAppRunner {
	public static void main(String[] args){
		boolean choiceFlag = true;
		Scanner inputChoice = new Scanner(System.in);
		System.out.println("++++++++++ Welcome To Day Care Center Management Application +++++++++");
		do{
			System.out.println("Please Secelt User Type\n 1. Admin \n 2. Care Provider \n 3. Exit \n Please Enter Your choice:\n ");
			int choice = 0;
			choice = Integer.parseInt(inputChoice.nextLine());
			switch(choice){
			case 1: 
				AdminController adminCtrl = new AdminController(inputChoice);
				adminCtrl.startOperations();
				break;
			case 2:
				CareProviderController careProviderCtrl = new CareProviderController();
				//careProviderCtrl.startOperations();
				break;
			case 3:
				choiceFlag = false;
				break;
			default:
				System.out.println("Please Enter Above Mentioned Option Only, Let's Try Again!!");
				break;
			}
		
		}while(choiceFlag);
		inputChoice.close();
		System.out.println("Thank You, We Are Done For The Day!");
		
	}

}
