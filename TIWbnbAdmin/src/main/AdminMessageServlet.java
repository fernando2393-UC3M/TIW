package main;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdminMessageServlet
 */
@WebServlet("/mensajes")
public class AdminMessageServlet extends HttpServlet {	
	private static final long serialVersionUID = 1L;
	
	/* Attributes */
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue adminQueue;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection _connection;
		Session _session;
		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);
			MessageProducer producer = _session.createProducer(adminQueue);
			
			TextMessage message = _session.createTextMessage();
			
			message.setText("Admin sends message");
			message.setStringProperty("sendBy", "admin");
			
			producer.send(message);	
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Message message = null;
		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			String selector = "sendBy = '" + "admin" + "'";
			MessageConsumer consumer = _session.createConsumer(adminQueue, selector);
			_connection.start();
			message = consumer.receive(500);			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextMessage tm = (TextMessage) message;
		
		// TODO Auto-generated method stub
		try {
			System.out.println(tm.getText());
			response.getWriter().append(tm.getText()).append(request.getContextPath());
			message.acknowledge();
		} catch (JMSException e) {
			System.err.println("No message or empty");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);		
	}
	

}
