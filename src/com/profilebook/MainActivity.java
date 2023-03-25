package com.profilebook;
import java.util.Scanner;
public class MainActivity {
	static int choice;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Hello welcome to Profile Book Application....");
		System.out.println("Choose the option please..");
		System.out.println("Enter 1. Login as user");
		System.out.println("Enter 2. Login as admin");
		System.out.println("Enter 3. Register as user");
		System.out.println("Enter 4. Register as admin");
		choice = scan.nextInt();
		User user = new User();
		Admin admin = new Admin();;
		switch(choice) {
		case 1:
			user.login();
			break;
		case 2:
			admin.login();
			break;
		case 3:
			user.register();
			break;
		case 4:
			admin.register();
			break;
			default:
				System.out.println("Enter valid number ....");
		}
	}

}
