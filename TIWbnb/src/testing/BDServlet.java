package testing;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Query;
import javax.persistence.EntityManagerFactory;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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

/**
 * Servlet implementation class BDServlet
 */
public class BDServlet extends HttpServlet {

	
	@PersistenceContext(unitName= "JPAexample2")
	private EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	 
////////////////////////////////////////////////////////////////////////////////////////
public void init() {

// Lee del contexto de servlet (Sesi�n a nivel de aplicaci�n)
ServletContext context = getServletContext();
}


////////////////////////////////////////////////////////////////////////////////////////
public void doGet(HttpServletRequest req, HttpServletResponse res) 
throws IOException, ServletException {
/*
	// Establece el Content Type
	PrintWriter out = res.getWriter();
	
	try {
		ut.begin();
	} catch (NotSupportedException | SystemException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	String stringQ1 = "SELECT s FROM Section s";
	Query query1 = em.createQuery(stringQ1);
	
	List results = query1.getResultList();
	//List<Cours> mylist = (List<Cours>) results.get(0);
	out.println("Query1:");
	for(Object obj: results){
		out.println( ((Section) obj).getName() );
	}
	
	}
	
	
	try {
		ut.commit();
	} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
			| HeuristicRollbackException | SystemException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	*/
	}

	////////////////////////////////////////////////////////////////////////////////////////  	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
	throws IOException, ServletException {    
	}
	}