package main;

import java.util.List;
import java.util.Locale.Category;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
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
	
	public User updateUserData(int id, String name, String surname, String birthdate, String password, String password1) {
		
		User user = null;
		
		if(password.equals(password1)) {
			
			
			String stringQ1 = "SELECT s FROM User s WHERE s.userId = '"+id+"'";
			
			Query query = em.createQuery(stringQ1);
			
			List<User> results = query.getResultList();
			
			user = results.get(0);
			
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			user.setUserName(name);
			user.setUserSurname(surname);
			user.setUserBirthdate(birthdate);
			user.setUserPassword(password);
			
			em.refresh(user);
			
			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return user;
	}
}
