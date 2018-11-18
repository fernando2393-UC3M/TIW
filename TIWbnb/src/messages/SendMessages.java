package messages;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import model.Admin;
import model.User;

public class SendMessages {
	
	/* Obtains a message from an admin to an user
	 * Receives Admin, User
	 * Needs Connection Factory and Queue
	 */
	public static void sendMessage(User sender, User receiver, String content, ConnectionFactory cf, Queue queue) throws JMSException{
		Connection _connection = null;
		Session _session;

		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			MessageProducer producer = _session.createProducer(queue);

			Message message = _session.createTextMessage(content);
			message.setStringProperty("sentTo", ""+receiver.getUserId());
			message.setStringProperty("sentBy", ""+sender.getUserId());
			message.setStringProperty("admin", "no");
			
			producer.send(message);	
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (_connection != null) {
            	_connection.close();
            }
        }
	}	
	
	/* Send a message to an admin
	 * Receives Admin, User
	 * Needs Connection Factory and Queue
	 */
	public static void sendMessageAdmin(Admin admin, User user, String content, ConnectionFactory cf, Queue queue) throws JMSException{
		Connection _connection = null;
		Session _session;

		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			MessageProducer producer = _session.createProducer(queue);

			Message message = _session.createTextMessage(content);
			message.setStringProperty("sentTo", ""+admin.getAdminId());
			message.setStringProperty("sentBy", ""+user.getUserId());
			message.setStringProperty("admin", "toAdmin");
			
			producer.send(message);	
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (_connection != null) {
            	_connection.close();
            }
        }
	}
}
