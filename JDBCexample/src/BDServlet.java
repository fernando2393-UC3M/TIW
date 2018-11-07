


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

String database = "";       
String servername = "localhost";
String port = "3306";
String username  = ""; // complete
String password  = ""; // complete

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
			// complete
	// 2- Obtain a Connection object --> con
	String url = "";
			// complete
	Connection con = null;

	if (con==null){
		System.out.println("--->UNABLE TO CONNECT TO SERVER:" + servername);
	} else {

	// 3- Obtain an Statement object -> st
		// complete

	// 4.- Execute the query "select * from users" 
		// complete
		
	// 5.- Iterate through the ResultSet obtained and add to the html page the id, name and surname of the users
		// complete
		
	// 6.- Close the statemente and the connection
		// complete
		
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