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
		// TODO Auto-generated method stub
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
				PreparedStatement pmst = con.prepareStatement("Insert into users values(?,?,?,?)");
				pmst.setInt(1, ++count);
				pmst.setString(2, userName);
				pmst.setString(3, password);
				pmst.setInt(4, 0);
				int count = pmst.executeUpdate();	
				if(count>0)
					System.out.println("Successfully registered ....");
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
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter user name : ");
		userName = scan.nextLine();
		System.out.print("Enter password : ");
		password = scan.nextLine();
		
		
		try {			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery("select * from users where username =\'"+userName+"\'");
//			System.out.println("here ");
			String dPassword;
			while(rs.next()) {
				dPassword = rs.getString(3);
				if(password.equals(dPassword)) { // password matches
					int count = smt.executeUpdate("update users set isactive ="+1+" where username =\'"+userName+"\'");
					System.out.println("Login successfully");
					String choice;
					System.out.println("Enter \"post\" for post. \n Enter 'view' for view.\nEnter \"quit\" for logout.");
			
					choice = scan.nextLine();
					if(choice.equalsIgnoreCase("post")) {
						post(userName);
					}
					if(!choice.equalsIgnoreCase("quit")) {
						view();
					}
					logout(userName);
					con.close();
					break;
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
			Statement smt = con.createStatement();
			int count = smt.executeUpdate("update users set isactive ="+0+" where username =\'"+userName+"\'");
			System.out.println("Logout succuessfully");
		} catch (SQLException e) {
			System.out.println(e);
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
				System.out.println("***************************");
				System.out.println(rs.getString(2));
				System.out.println("Created in : "+rs.getString(3));
				System.out.println("Created by : "+rs.getString(4));
				System.out.println("***************************");
				System.out.print("Do you want to like this [yes/no]?");
				String s = scan.nextLine();
				if(s.equalsIgnoreCase("yes")){
					int count = smt.executeUpdate("update posts set likes ="+(rs.getInt(4)+1)+" where text =\'"+rs.getString(2)+"\'");
				}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void post(String userName) {
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter text to post....");
		String text = scan.nextLine();
		LocalDate date = LocalDate.now();
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","9020");
			PreparedStatement pmst = con.prepareStatement("Insert into posts values(?,?,?,?,?)");
			pmst.setInt(1, ++p);
			pmst.setString(2, text);
			pmst.setString(3, date.toString());
			pmst.setInt(4, count);
			pmst.setString(5, userName);
			int temp = pmst.executeUpdate();
			System.out.println("Successfully posted....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
