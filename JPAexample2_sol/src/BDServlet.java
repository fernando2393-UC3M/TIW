

import entities.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Query;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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


// To be used for Glassfish
import java.util.Properties;

import org.eclipse.persistence.config.PersistenceUnitProperties;


/*** PACKAGES TO IMPORT WHEN USING JPA
import javax.persistence.EntityManager;

import javax.persistence.Persistence;

import javax.persistence.EntityTransaction;
****/


/**
 * Servlet implementation class BDServlet
 */
public class BDServlet extends HttpServlet {

	 
////////////////////////////////////////////////////////////////////////////////////////
public void init() {

// Lee del contexto de servlet (Sesi�n a nivel de aplicaci�n)
ServletContext context = getServletContext();
}


////////////////////////////////////////////////////////////////////////////////////////
public void doGet(HttpServletRequest req, HttpServletResponse res) 
throws IOException, ServletException {

	/****************************************************************************
	 ****************** CREATION OF A NEW USER USING JPA ************************
	 ****************************************************************************/

	// Uncomment when using Glassfish

	Properties properties = new Properties();
	properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
	properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS, "JPAexample1"); // fill with the name of your persistence unit

	// 1 Create the factory of Entity Manager
	// To use when using Glassfish
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAexample1", properties); // fill with the name of your persistence unit

	// 2 Create the Entity Manager
	EntityManager em = emf.createEntityManager();
	
	// 3 Get one EntityTransaction and start it
	EntityTransaction et = em.getTransaction();
	et.begin();
	
	// Create one entity user, set its attributes and make it persist
	Query q = em.createQuery("Select s.name from Section s");
	
	List queryResults = q.getResultList();
	
	String s = "";
	for (Object  o : queryResults){
		s = (String) o;
		System.out.println("Name: "+s);		
	}
	
	q = em.createQuery("Select o.name from LObject o JOIN o.courses c WHERE c.idCourse = 18 ");
	
	queryResults = q.getResultList();
	
	for (Object  o : queryResults){
		s = (String) o;
		System.out.println("NameObject: "+s);		
	}
	
	entities.LObject myObject = new entities.LObject();
	myObject.setName("New One2");
	myObject.setRoute("Nothing2");
	myObject.setDescription("Something here2..");
	Cours c19 = em.find(Cours.class, 19);
	
	List<Cours> hashCourses = new ArrayList<Cours>();
	hashCourses.add(c19);
	myObject.setCourses(hashCourses);
	
	em.persist(myObject);

	et.commit();
	em.close();
	
	// 4 Commit the transaction

	// 5 Close the manager	
	
	
	/****************************************************************************
	 ****************** RETRIEVE THE USERS USING JDBC ***************************
	 ****************************************************************************/
		
	
	
	String database = "usersdb";       
	String servername = "localhost";
	String port = "3306";
	String username  = "root"; // complete
	String password  = "admin"; // complete

	// Establece el Content Type
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();

	out.println("<HTML>");
	out.println("<HEAD><TITLE>BDServlet</TITLE></HEAD>");
	out.println("<BODY bgcolor=\"#ffff66\">");
	out.println("<H1><FONT color=\"#666600\">Database: Users</FONT></H1></BR>");
	out.println("<FORM METHOD=\"POST\" ACTION=\"" + "\">"); // Se llama as� mismo por POST        


	try {


		// 1- Load driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// 2- Obtain a Connection object --> con
		String url = "jdbc:mysql://"+servername+":"+port+"/"+database;  
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/usersdb", username, password);
		if (con==null){
			System.out.println("--->UNABLE TO CONNECT TO SERVER:" + servername);
		} else {
		 
		
		// 3- Obtain an Statement object -> st
		
		Statement st = con.createStatement();

		// Retrieve users from the ResultSet --> rs
			ResultSet rs = st.executeQuery("Select * from CoursesObjects");
			
		 out.println("<FONT color=\"#ff0000\">Course - Object</FONT><BR>");
		 while (rs.next()){
			out.println("<FONT color=\"#ff0000\">"+rs.getInt("id_course")+"  "+rs.getInt("id_object")+"</FONT><BR>");
		 }
		 
		}
	} catch (Exception e) {
		out.println("<FONT color=\"#ff0000\">"+e.getMessage()+"</FONT><BR>");
	}


	out.println("</FORM>");
	out.println("</BODY></HTML>");

	out.close();
	}

	////////////////////////////////////////////////////////////////////////////////////////  	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
	throws IOException, ServletException {    
	}
	}