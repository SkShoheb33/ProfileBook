package com.profilebook;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
public class User {
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
			System.out.println("Password updated");
			try {			
				Class.forName("oracle.jdbc.driver.OracleDriver");
//				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
				Statement smt = con.createStatement();
			
				PreparedStatement pmst = con.prepareStatement("Insert into users values(?,?,?,?,?)");
				// this method used to prepare the  SQL queries 
				pmst.setInt(1, count++);
				pmst.setString(2, userName);
				pmst.setString(3, password);
				pmst.setInt(4, 0);
				pmst.setString(5, "guest");
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
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery("select * from users where username =\'"+userName+"\'");
			if(!rs.next()) {
				System.out.println("No user name in the database");
				register();
			}
			String dPassword;
			dPassword = rs.getString(3);
			if(password.equals(dPassword)) { // password matches
				int count = smt.executeUpdate("update users set isactive ="+1+" where username =\'"+userName+"\'");
				System.out.println("Login successfully");
				showMenu();
			}else {
				System.out.println("There is no user with this credentials..");
				System.out.print("Do you want to create new account[yes/no]?");
				String choice = scan.nextLine();
				if(choice.equalsIgnoreCase("yes")) {
					register();
				}else {
					login();
				}
			}
			
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	public void logout(String userName) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
			Statement smt = con.createStatement();
			int count = smt.executeUpdate("update users set isactive ="+0+" where username =\'"+userName+"\'");
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
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
			Statement smt = con.createStatement();
			int count = smt.executeUpdate("update posts set likes ="+(rs.getInt(4)+1)+" where text =\'"+rs.getString(2)+"\'");
		}catch(Exception e) {
			
		}
	}
	
	public void view() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Scanner scan = new Scanner(System.in);
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
			Statement smt = con.createStatement();//actually the post has text and created by and created in time 
			ResultSet rs = smt.executeQuery("select * from posts");
			while(rs.next()) {
				System.out.println("***************************\n");
				System.out.println(rs.getString(2)+"\n");
				System.out.println("Created in : "+rs.getString(3));
				System.out.println("Created by : "+rs.getString(5));
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
	
	public void post(String userName) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter text to post....");//yes
		String text = scan.nextLine();
		LocalDate date = LocalDate.now();
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
//			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/w3d3","localhost","root");
			PreparedStatement pmst = con.prepareStatement("Insert into posts values(?,?,?,?,?)");
			pmst.setInt(1, ++p);
			pmst.setString(2, text);
			pmst.setString(3, date.toString());
			pmst.setInt(4, count);
			pmst.setString(5, userName);
			int temp = pmst.executeUpdate();
			System.out.println("Successfully posted....");
			System.out.print("Do you want to post another [yes/no]? ");
			String choice = scan.nextLine();
			if(choice.equalsIgnoreCase("yes")) {
				post(userName);
			}
			showMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showMenu() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter \"post\" for post.\nEnter 'view' for view.\nEnter \"logout\" for logout.");
		String choice = scan.nextLine();
		if(choice.equalsIgnoreCase("post")) {
			post(userName);
		}
		if(choice.equalsIgnoreCase("view")) {
			view();
		}
		if(choice.equalsIgnoreCase("logout")) {
			logout(userName);
		}
	}
}
