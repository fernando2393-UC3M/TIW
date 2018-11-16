package main;

import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
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

import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

/**
 * Servlet implementation class BDServlet
 */
@WebServlet(urlPatterns = {"/index", "/admin", 
				"/resultados", "/renting", "/delete",
				"/registrado", "/mensajes", "/login", "/register",
				"/alojamiento", "/casa", "/viajes", "/logout"})
public class BNBServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="TIWbnb")
	private EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	String path = "http://localhost:8080/TIWbnb/";
	
	ServletContext context;
	
	HttpSession session;
	
	 
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
		else if(requestURL.equals(path+"delete")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");		
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		else if(requestURL.equals(path+"register")){
			ReqDispatcher =req.getRequestDispatcher("registrado.jsp");
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
		else if(requestURL.equals(path+"logout")){
			doPost(req, res); // Special case -------------------------
			return;
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
		
		//------------------------PROFILE LOGIN------------------------
		
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

				// Save user in servlet session
				
				session = req.getSession();

				try {
					session.setAttribute("user", result.getInt("USER_ID"));
					session.setMaxInactiveInterval(30*60); // 30 mins
					
					Cookie user = new Cookie("id", Integer.toString(result.getInt("USER_ID")));
					user.setMaxAge(30*60);
					
					res.addCookie(user);
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
		
		//------------------------USER REGISTRATION------------------------
		
		else if(requestURL.toString().equals(path+"register")) {
			
			dispatcher = req.getRequestDispatcher("index.jsp");

			RegisterUser register = new RegisterUser();

			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int registered = register.register(em, req.getParameter("registerEmail"), req.getParameter("registerName"),
					req.getParameter("registerSurname"),req.getParameter("registerPassword")); // Deletion method

			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(registered == 1) {
				req.setAttribute("Registered", 1);
			}
			else if (registered == 2) {
				req.setAttribute("Registered", 2);
			}

			dispatcher.forward(req, res);			

		}

		//------------------------INFORMATION UPDATE------------------------

		else if(requestURL.toString().equals(path+"registrado")) {
			
			
			dispatcher = req.getRequestDispatcher("registrado.jsp");
			
			int id = (int) session.getAttribute("user");
			
			
			Registrado registradoInstance = new Registrado();
			
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			boolean updated = registradoInstance.updateUserData(id, req.getParameter("name"), req.getParameter("surname"), 
					req.getParameter("birthdate"), req.getParameter("password"), req.getParameter("password1"), em);			

			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			User user = em.find(User.class, id); // Select the user after commit

			req.setAttribute("Name", user.getUserName());
			req.setAttribute("Surname", user.getUserSurname());			
			req.setAttribute("Birthdate", (new SimpleDateFormat("yyyy-MM-dd")).format(user.getUserBirthdate()));
			req.setAttribute("Password", user.getUserPassword());
			
			if(updated) {
				req.setAttribute("Updated", 1);
			}
			else {
				req.setAttribute("Updated", 2);
			}

			dispatcher.forward(req, res);			
			
		}
		
		//------------------------PROFILE DELETION------------------------

		else if(requestURL.toString().equals(path+"delete")) {
			
			dispatcher = req.getRequestDispatcher("index.jsp");

			int id = (int) session.getAttribute("user");

			DeleteUser delete = new DeleteUser();

			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			delete.deletion(id, em); // Deletion method

			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			session.removeAttribute("user"); // Remove user from session

			dispatcher.forward(req, res);			

		}
		
		//------------------------HOUSE CREATION------------------------

		else if(requestURL.toString().equals(path+"casa")){
			
			dispatcher = req.getRequestDispatcher("casa.jsp");
			
			AddHouse houseInstance = new AddHouse();
			
			houseInstance.openConnection();
			
			String str= houseInstance.RegisterHouse(req.getParameter("houseName"), req.getParameter("houseDesc"));
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
		
		//-----------------------LOGOUT-------------------------------
		
		else if(requestURL.toString().equals(path+"logout")) {
			
			req.removeAttribute("Name");
			req.removeAttribute("Surname");
			req.removeAttribute("Birthdate");
			req.removeAttribute("Password");
			
			session = req.getSession(false);
			
			if(session != null) {
				session.removeAttribute("user");
				session.invalidate();
			}
			
			dispatcher = req.getRequestDispatcher("index.jsp");
			dispatcher.forward(req, res);
			
		}
		
	}
}