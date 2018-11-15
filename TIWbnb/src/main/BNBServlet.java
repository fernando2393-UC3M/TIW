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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
				"/alojamiento", "/casa", "/viajes"})
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
		else if(requestURL.equals(path+"viajes")){
			ReqDispatcher =req.getRequestDispatcher("viajes.jsp");
		} 
		else {
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		ReqDispatcher.forward(req, res);
	}

	////////////////////////////////////////////////////////////////////////////////////////  	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user

		String requestURL = req.getRequestURL().toString();


		if(requestURL.toString().equals(path+"registrado")) {
			dispatcher = req.getRequestDispatcher("registrado.jsp");
			ResultSet result = (ResultSet) context.getAttribute("User");
			HttpSession session = req.getSession();
			
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
				session.removeAttribute("user");
				session.setAttribute("user", user);
			}
			
			dispatcher.forward(req, res);			
			
		}

		else if(requestURL.toString().equals(path+"casa")){
			dispatcher = req.getRequestDispatcher("casa.jsp");
			AddHouse houseInstance = new AddHouse();
			String str= houseInstance.RegisterHouse(req.getParameter("houseName"), req.getParameter("housePass"));
			System.out.println(str);
			
			if(str != null){
				dispatcher = req.getRequestDispatcher("viajes.jsp");
				dispatcher.forward(req, res);
			}
			else{
				dispatcher = req.getRequestDispatcher("mensajes.jsp");
				dispatcher.forward(req, res);
			}
			
		}
	}
}