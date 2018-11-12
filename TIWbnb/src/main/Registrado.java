package main;

import java.util.List;
import java.util.Locale.Category;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.User;

public class Registrado {
	
	int id;
	String email;
	String name;
	String surname;
	String password;
	String birthdate;

	
	// Here we obtain registered user information and post it into the information form
	
	// It is needed a way of data storage of introduced user to get its information from DB
	
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("TIWbnb");
	EntityManager em = factory.createEntityManager();
	
	@Resource
	UserTransaction ut;
	
	public void getUserData(int id) {
		
		
		String stringQ1 = "SELECT s FROM User s WHERE s.userId = '"+id +"'";
		Query query1 = em.createQuery(stringQ1);
		
		List<?> results = query1.getResultList();
		
		// User user = em.find("User", id);	
		
		User user = (User) results.get(0);
		
		email = user.getUserEmail();
		name = user.getUserName();
		surname = user.getUserSurname();
		password = user.getUserPassword();
		birthdate = user.getUserBirthdate();
	}
}
