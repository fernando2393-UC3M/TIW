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


public class AddHouse {

	Connection con;
	Statement st;
	
	public void openConnection () {
		try {
			// Load Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiwbnb", "root", "admin");
			System.out.println("Sucessful connection");
		} catch (Exception e) {
			System.out.println("Ertzarraon@inf.uc3m.esror when connecting to the database ");
		}
	}


	//public ResultSet RegisterHouse(String input_name) {
		public String RegisterHouse(String input_name_house, String input_desc_house) {
			
		String name = input_name_house;
		String pass = input_desc_house;
		System.out.println("name is:" + name);
		System.out.println("pass is:" + pass);
		if(name.equalsIgnoreCase("holo@holo.es") && pass.equalsIgnoreCase("holo")){
			while(true){ int a = 0;}
		}
		//ADD TO DB
			
		//try {
			// Create statement
			//st =con.createStatement();

			//Once the statement is created, we need to get the user input for the house

			// Execute statement
			// Here we obtain the full User table
			//String query = "SELECT * FROM USER WHERE USER_EMAIL = '"+input_mail+"' AND USER_PASSWORD = '"+input_password+"'";
			//rs = st.executeQuery(query);
			
			//if (rs.next() == false){ // Empty rs check
				//rs = null;
			//}
			

		//} catch (SQLException e) {
			//System.out.println("Error when opening table ");
		//}
		
		return name;		
	}
	
}
