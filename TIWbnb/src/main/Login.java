package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login {

	Connection con;
	Statement st;
	ResultSet rs;

	public void openConnection () {
		try {
			// Load Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiwbnb", "root", "admin");
			System.out.println("Sucessful connection");
		} catch (Exception e) {
			System.out.println("Error when connecting to the database ");
		}
	}

	public void retrieveTableDataUser() {
		try {
			// Create statement
			st =con.createStatement();

			//Once the statement is created, we need to get the user input for both user email and password

			// Execute statement
			// Here we obtain the full User table 
			rs = st.executeQuery("SELECT * FROM USER ORDER BY USER_ID");

		} catch (SQLException e) {
			System.out.println("Error when opening table ");
		}
	}
	
	public void retrieveTableDataAdmin() {
		try {
			// Create statement
			st =con.createStatement();

			//Once the statement is created, we need to get the user input for both user email and password

			// Execute statement
			// Here we obtain the full User table 
			rs = st.executeQuery("SELECT * FROM ADMIN ORDER BY ADMIN_ID");

		} catch (SQLException e) {
			System.out.println("Error when opening table ");
		}
	}

	public int Check(String input_mail, String input_password) {
		
		// Default value --> No match found
		int ret = -1;
		
		// Retrieve table for users
		
		retrieveTableDataUser();
		
		try {
			while (rs.next()) { // Iterate for all the entries of the User table
				String mail = rs.getString("USER_EMAIL");
				String password = rs.getString("USER_PASSWORD");

				// Check for login

				if (input_mail.equals(mail)) {
					if (input_password.equals(password)) {
						return ret = 0;
						//Login performed
					}
				}
			}
			
			System.out.println("No user with match");			
			
		}
		catch (Exception e) {
			System.out.println("Error when visualizing information");
		}
		
		// Check for administrator
		
		// Retrieve table for administrator
		
		retrieveTableDataAdmin();
		
		try {
			while (rs.next()) { // Iterate for all the entries of the User table
				String mail = rs.getString("ADMIN_EMAIL");
				String password = rs.getString("ADMIN_PASSWORD");

				// Check for login

				if (input_mail.equals(mail)) {
					if (input_password.equals(password)) {
						return ret = 1;
						//Login performed
					}
				}
			}
			
			System.out.println("No admin with match");			
			
		}
		catch (Exception e) {
			System.out.println("Error when visualizing information");
		}
		
		return ret;
	}
}
