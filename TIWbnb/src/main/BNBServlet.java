package main;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.sql.Date;
import messages.ReadMessages;
import model.Home;
import model.MessagesAdmin;
import model.User;
import javax.jms.JMSException;

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
	protected EntityManager em;
	
	@Resource
	private UserTransaction ut;
	
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue queue;
	
	String path = "http://localhost:8080/TIWbnb/";
	
	ServletContext context;
	
	HttpSession session;
	
	public void persist(Object entity) {
		try {
			ut.begin();
			em.persist(entity);
			ut.commit();
		} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
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

//		if(requestURL.toString().equals(path+"admin")){
//			ReqDispatcher =req.getRequestDispatcher("admin.jsp");
//		}
//		else 
		if(requestURL.equals(path+"alojamiento")){
			ReqDispatcher =req.getRequestDispatcher("alojamiento.jsp");
		}
		else if(requestURL.equals(path+"casa")){
			ReqDispatcher =req.getRequestDispatcher("casa.jsp");
		}
		else if(requestURL.equals(path+"mensajes")){
			
			//------------------------READ MESSAGES------------------------
					
			// Get userId from session (need parameter name to access)
			int userId = (Integer) session.getAttribute("user"); 
			
			try {
				ut.begin();
				List<model.Message> messageList;
				messageList = ReadMessages.getMessages(userId, em, cf, queue);
				ReadMessages.setRead(userId, em);
				ut.commit();

				ut.begin();
				List<MessagesAdmin> messageAdminList;
				messageAdminList = ReadMessages.getMessagesAdmin(userId, em, cf, queue);
				ReadMessages.setRead(userId, em);
				ut.commit();
				
				// TODO Save messages in user session
				if(messageList.size() > 0)
					session.setAttribute("UserMessages", messageList); 
				
				// TODO Save admin messages in user session
				if(messageAdminList.size() > 0)
					session.setAttribute("AdminMessages", messageAdminList); 
				
			} catch (JMSException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Treat JMS/JPA Exception
			}
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");				
		
			//------------------------END READ MESSAGES------------------------
			
		}
		else if(requestURL.equals(path+"registrado")){
			int id = (int) session.getAttribute("user");
						
			User user = em.find(User.class, id); // Select the user after commit

			req.setAttribute("Name", user.getUserName());
			req.setAttribute("Surname", user.getUserSurname());			
			req.setAttribute("Birthdate", (new SimpleDateFormat("yyyy-MM-dd")).format(user.getUserBirthdate()));
			req.setAttribute("Password", user.getUserPassword());
			
			ReqDispatcher =req.getRequestDispatcher("registrado.jsp");	
		}
		else if(requestURL.equals(path+"delete")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");		
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		else if(requestURL.equals(path+"register")){
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
			
			String queryS = "SELECT s FROM User s WHERE s.userEmail = '"+req.getParameter("registerEmail")+"'";
			Query query = em.createQuery(queryS);
			List <User> userList = query.getResultList();
			
			if(userList.isEmpty()){
				
				User user = new User();
								
				@SuppressWarnings("deprecation")
				Date aux = new Date(1970, 01, 01);
				
				// User id automatically generated by MySQL
				user.setUserEmail(req.getParameter("registerEmail"));
				user.setUserName(req.getParameter("registerName"));
				user.setUserSurname(req.getParameter("registerSurname"));
				user.setUserPassword(req.getParameter("registerPassword"));
				user.setUserBirthdate(aux);
				
				persist(user);
				
				req.setAttribute("Registered", 1);
			}
			
			else {
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
			
			if(req.getParameter("houseName") != null && req.getParameter("houseCity") != null && req.getParameter("houseDesc") != null && req.getParameter("houseSubDesc") != null && 
					req.getParameter("houseType") != null && req.getParameter("guests") != null && req.getParameter("photo") != null && req.getParameter("inputPriceNight") != null &&
					 req.getParameter("iDate") != null && req.getParameter("fDate") != null){
				
				int guests = Integer.parseInt(req.getParameter("guests"));
				String aux = "";
				
				aux = req.getParameter("inputPriceNight");
				
				BigDecimal inputPriceNight = new BigDecimal(aux.replaceAll(",",""));
				
				Home home = new Home();
				
				String iDate = req.getParameter("iDate");
				String fDate = req.getParameter("fDate");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date parsedIDate = new Date(1970, 01, 01);
				java.util.Date parsedFDate = new Date(1970, 01, 01);
				try {
					parsedIDate = format.parse(iDate);
					parsedFDate = format.parse(fDate);
				} catch (ParseException e) {
				}
				
				String photography = req.getParameter("photo");
				
				// House id automatically generated by MySQL
				home.setHomeName(req.getParameter("houseName"));
				home.setHomeCity(req.getParameter("houseCity"));
				home.setHomeDescriptionFull(req.getParameter("houseDesc"));
				home.setHomeDescriptionShort(req.getParameter("houseSubDesc"));
				home.setHomeType(req.getParameter("houseType"));
				home.setHomeGuests(guests);
				home.setHomePhotos(req.getParameter("photo"));
				home.setHomePriceNight(inputPriceNight);
				home.setHomeAvDateInit(parsedIDate);
				home.setHomeAvDateFin(parsedFDate);

				persist(home);
				
				
//				houseInstance.RegisterHouse(em, req.getParameter("houseName"), req.getParameter("houseCity"), req.getParameter("houseDesc"), req.getParameter("houseSubDesc")
//						, req.getParameter("houseType"), guests, req.getParameter("photo"), inputPriceNight, req.getParameter("iDate"), req.getParameter("fDate"));
				
			}
			
			dispatcher.forward(req, res);	
			
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