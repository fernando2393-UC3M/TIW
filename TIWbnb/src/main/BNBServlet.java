package main;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Query;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.EntityManagerFactory;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.servlet.RequestDispatcher;
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
// To be used for Glassfish
import java.util.Properties;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import model.User;

/**
 * Servlet implementation class BDServlet
 */
@WebServlet(urlPatterns = {"/index", "/admin", 
				"/resultados", "/renting", 
				"/registrado", "/mensajes", 
				"/alojamiento", "/casa", "/login"})
public class BNBServlet extends HttpServlet {
	
	@PersistenceContext(unitName="TIWbnb")
	private EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	String path = "http://localhost:8080/TIWbnb/";
	
	ServletContext context;
	
	 
////////////////////////////////////////////////////////////////////////////////////////
	public void init() {

		// It reads servelt's context

		context = getServletContext();
	}


////////////////////////////////////////////////////////////////////////////////////////
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher ReqDispatcher;

		String requestURL = req.getRequestURL().toString();

		if(requestURL.toString().equals(path+"admin")){
			ReqDispatcher =req.getRequestDispatcher("admin.jsp");
		}
		else if(requestURL.equals(path+"alojamiento")){
			ReqDispatcher =req.getRequestDispatcher("alojamiento.jsp");
		}
		else if(requestURL.equals(path+"casa")){
			ReqDispatcher =req.getRequestDispatcher("casa.jsp");
		}
		else if(requestURL.equals(path+"mensajes")){
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");
		}
		else if(requestURL.equals(path+"registrado")){
			ReqDispatcher =req.getRequestDispatcher("registrado.jsp");		
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		else if(requestURL.equals(path+"renting")){
			ReqDispatcher =req.getRequestDispatcher("renting.jsp");					
		}
		else if(requestURL.equals(path+"resultados")){
			ReqDispatcher =req.getRequestDispatcher("resultados.jsp");				
		}
		else {
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		ReqDispatcher.forward(req, res);


		/* Standard Query*/
		/*
	// Establece el Content Type
	PrintWriter out = res.getWriter();

	try {
		ut.begin();
	} catch (NotSupportedException | SystemException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	String stringQ1 = "SELECT s FROM User s";
	Query query1 = em.createQuery(stringQ1);

	List results = query1.getResultList();
	out.println("Query1:");
	for(Object obj: results){
		out.println( ((User) obj).getUserName() );
	}


	try {
		ut.commit();
	} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
			| HeuristicRollbackException | SystemException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}*/
	}

	////////////////////////////////////////////////////////////////////////////////////////  	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user

		String requestURL = req.getRequestURL().toString();

		// Login case

		if(requestURL.toString().equals(path+"login")){
			Login loginInstance = new Login();
			loginInstance.openConnection();
			ResultSet result = loginInstance.CheckUser(req.getParameter("loginEmail"), req.getParameter("loginPassword"));

			if (result != null) { //User match
				dispatcher = req.getRequestDispatcher("registrado.jsp");
				try {
					req.setAttribute("Name", result.getString("USER_NAME"));
					req.setAttribute("Surname", result.getString("USER_SURNAME"));
					req.setAttribute("Birthdate", result.getString("USER_BIRTHDATE"));
					req.setAttribute("Password", result.getString("USER_PASSWORD"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Save user in servlet context

				context.setAttribute("User", result); 

				// Forward to requested URL by user
				dispatcher.forward(req, res);				
			}

			else { // No user match
				dispatcher = req.getRequestDispatcher("index.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}
		}
		
		else if(requestURL.toString().equals(path+"registrado")) {
			dispatcher = req.getRequestDispatcher("registrado.jsp");
			ResultSet result = (ResultSet) context.getAttribute("User");
			
			int id = 0;
			
			try {
				id = result.getInt("USER_ID");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			Registrado registradoInstance = new Registrado();
			User user = registradoInstance.updateUserData(id, req.getParameter("name"), req.getParameter("surname"), 
					req.getParameter("birthdate"), req.getParameter("password"), req.getParameter("password1"));
			
			if(user!=null) {
				context.removeAttribute("User");
				context.setAttribute("User", user);
			}
			
			dispatcher.forward(req, res);			
			
		}
	}
}