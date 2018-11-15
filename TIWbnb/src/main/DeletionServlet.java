package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import model.User;

/**
 * Servlet implementation class DeletionServlet
 */
@WebServlet("/delete")
public class DeletionServlet extends HttpServlet {
	
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

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		RequestDispatcher ReqDispatcher = null;

		String requestURL = req.getRequestURL().toString();
		
		if(requestURL.toString().equals(path+"delete")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		
		ReqDispatcher.forward(req, res);
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user

		String requestURL = req.getRequestURL().toString();
		
		if(requestURL.toString().equals(path+"delete")){
			
			DeleteUser delete = new DeleteUser();
			
			if(context.getAttribute("User") instanceof User) {
				User user = (User) context.getAttribute("User");
				
				delete.openConnection();			
				delete.deletion(user.getUserId());
				delete.closeConnection();
				
				context.removeAttribute("User");
			}
			
			else if(context.getAttribute("User") instanceof ResultSet) {
				
				ResultSet user = (ResultSet) context.getAttribute("User");
				
				delete.openConnection();			
				try {
					delete.deletion(user.getInt("USER_ID"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delete.closeConnection();
				
				context.removeAttribute("User");
			}
			
			
		}
		
		dispatcher.forward(req, res);
		
	}

}
