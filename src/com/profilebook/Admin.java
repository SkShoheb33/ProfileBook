package com.profilebook;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Admin {
	String userName,password,cPassword;
	static int count = 1,p = 0;
	public void register() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter user name : ");
		userName = scan.nextLine();
		System.out.print("Enter password : ");
		password = scan.nextLine();
		System.out.print("Confirm password : ");
		cPassword = scan.nextLine();
		if(password.equals(cPassword)) {
			try {			
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
				Statement smt = con.createStatement();
				PreparedStatement pmst = con.prepareStatement("Insert into admins values(?,?,?,?)");
				pmst.setInt(1, ++count);
				pmst.setString(2, userName);
				pmst.setString(3, password);
				pmst.setInt(4, 0);
				int count = pmst.executeUpdate();	
				if(count>0) {
					System.out.println("Successfully registered ....");
					System.out.print("Do you want to login[yes/no]?");
					String choice = scan.nextLine();
					if(choice.equalsIgnoreCase("yes")) {
						login();
					}
				}
				else
					System.out.println("Not registered.....");
				
			}catch(Exception e) {
				System.out.println(e);
			}
		}else {
			System.out.println("Enter same password...");
			register();
		}
		
	}

	public void login() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter user name : ");
		userName = scan.nextLine();
		System.out.print("Enter password : ");
		password = scan.nextLine();
		
		
		try {			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery("select * from admins where adminname =\'"+userName+"\'");
//			System.out.println("here ");
			String dPassword;
			if(!rs.next()) {
				System.out.println("No admin name in the database");
				register();
			}
			
			dPassword = rs.getString(3);
			if(password.equals(dPassword)) { // password matches
				int count = smt.executeUpdate("update admins set isactive ="+1+" where adminname =\'"+userName+"\'");
				System.out.println("Login successfully");
				showMenu();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	public void logout(String userName) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			int count = smt.executeUpdate("update admins set isactive ="+0+" where username =\'"+userName+"\'");
			System.out.println("Logout succuessfully");
			con.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		System.out.println("Thank you....");
	}
	
	public void like(ResultSet rs) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			int count = smt.executeUpdate("update posts set likes ="+(rs.getInt(4)+1)+" where text =\'"+rs.getString(2)+"\'");
		}catch(Exception e) {
			
		}
	}
	
	public void view() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Scanner scan = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery("select * from posts");
			while(rs.next()) {
				System.out.println("***************************\n");
				System.out.println(rs.getString(2)+"\n");
				System.out.println("Created in : "+rs.getString(3));
				System.out.println("Created by : "+rs.getString(4));
				System.out.println("\n***************************");
				System.out.print("Do you want to like this [yes/no]?");
				String s = scan.nextLine();
				if(s.equalsIgnoreCase("yes")){
					like(rs);
				}
			}
			System.out.println("You have reached last post...");
			showMenu();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void createGroup(String userName) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			Scanner scan = new Scanner(System.in);
			System.out.println("Do you want the user to add/delete/change the group ? ");
			String choice = scan.nextLine();
			if(choice.equalsIgnoreCase("add")) {
				System.out.println("Enter group name : ");
				String groupName = scan.nextLine();
				System.out.print("Enter user name to join the group : ");
				String user = scan.nextLine();
				int count = smt.executeUpdate("update users set groupname = \'"+groupName+"\' where username =\'"+user+"\'");
			}else if(choice.equalsIgnoreCase("delete")) {
				System.out.print("Enter user name to remove from the group : ");
				String user = scan.nextLine();
				int count = smt.executeUpdate("update users set groupname =guest where username =\'"+user+"\'");
			}else {
				System.out.print("Enter old group name : ");
				String oldGroupName = scan.nextLine();
				System.out.print("Enter new group name : ");
				String newGroupName = scan.nextLine();
				int count = smt.executeUpdate("update users set groupname =\'"+newGroupName+"\' where groupname =\'"+oldGroupName+"\'");
			}
			System.out.println("Successfully updated....");
			showMenu();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void showMenu() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter \"create\" for create groups.\nEnter 'view' for view.\nEnter \"logout\" for logout.");
		String choice = scan.nextLine();
		if(choice.equalsIgnoreCase("create")) {
			createGroup(userName);
		}
		if(choice.equalsIgnoreCase("view")) {
			view();
		}
		if(choice.equalsIgnoreCase("logout")) {
			logout(userName);
		}
	}
}
