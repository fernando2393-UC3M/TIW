package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import java.util.Locale.Category;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class DeleteUser {
	
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
			System.out.println("Error when connecting to the database ");
		}
	}
	
	public void deletion(int id) {
		
		try {
			st =con.createStatement();
			
			String query = "DELETE FROM USER WHERE USER_ID = '"+id+"'";
			st.executeQuery(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
