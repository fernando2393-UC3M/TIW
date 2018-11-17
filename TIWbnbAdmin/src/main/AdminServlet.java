package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

import model.MessagesAdmin;
import model.User;

@WebServlet(urlPatterns = {"/admin", "/resultados", "/casa", "/manage_users", "/mensajes",  "/modify", "/index", "/login"})

public class AdminServlet extends HttpServlet {
	
	/* Attributes */
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue adminQueue;
	
	@PersistenceContext(unitName="TIWbnbAdmin")
	private EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	String path = "http://localhost:8080/TIWbnbAdmin/";
	
	HttpSession session;
	
	ServletContext context;
	
	public void init() {

		// It reads servlet's context

		context = getServletContext();
	}
	

	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher ReqDispatcher;

		String requestURL = req.getRequestURL().toString();

		if(requestURL.toString().equals(path+"admin")){
			ReqDispatcher =req.getRequestDispatcher("admin.jsp");
		}
	
		//------------------------READ MESSAGES------------------------
		
		else if(requestURL.equals(path+"mensajes")){		
			//TODO: get adminId from session (need parameter name to access)
			int adminId = 1;
			List<MessagesAdmin> messageList = new ArrayList();
			try {
				ut.begin();
				messageList = ReadMessages.getMessages(adminId, em, cf, adminQueue);
				ReadMessages.setRead(adminId, em);
				ut.commit();
				
			} catch (JMSException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Treat JMS/JPA Exception
			}
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");
			
			
		}
	
		//------------------------END READ MESSAGES------------------------
		
		else if(requestURL.equals(path+"resultados")){
			ReqDispatcher =req.getRequestDispatcher("resultados.jsp");
		}
		else if(requestURL.equals(path+"modify")){
			ReqDispatcher =req.getRequestDispatcher("manage_users.jsp");
		}
		else if(requestURL.equals(path+"manage_users")){			
			ReqDispatcher =req.getRequestDispatcher("manage_users.jsp");
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		else {
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		ReqDispatcher.forward(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user

		String requestURL = req.getRequestURL().toString();

		// Login case

		if(requestURL.toString().equals(path+"login")){
			Login loginInstance = new Login();
			loginInstance.openConnection();
			ResultSet result = loginInstance.CheckAdmin(req.getParameter("loginEmail"), req.getParameter("loginPassword"));

			if (result != null) { //User match
				dispatcher = req.getRequestDispatcher("admin.jsp");
				
				session = req.getSession();
				
				// Save user in servlet context
				try {
					session.setAttribute("admin", result.getInt("ADMIN_ID"));
					session.setMaxInactiveInterval(30*60); // 30 mins
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Forward to requested URL by user
				dispatcher.forward(req, res);				
			}

			else { // No user match
				dispatcher = req.getRequestDispatcher("index.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}
		}
		
		else if (requestURL.toString().equals(path+"modify")){
			
			Modify modify = new Modify();
			dispatcher = req.getRequestDispatcher("manage_users.jsp");
			
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String aux = req.getParameter("inputId");
			int id = Integer.parseInt(aux);
			
			modify.updateUserData(id, req.getParameter("inputName"), req.getParameter("inputSurname"), 
					req.getParameter("inputBirthdate"), req.getParameter("inputPassword"), em);
			
			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			dispatcher.forward(req, res);
			
		}
	}
}