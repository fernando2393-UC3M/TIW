package main;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import java.util.Locale.Category;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.User;

public class RegisterUser {
	
	public int register(EntityManager em, String email, String name, String surname, String password) {
		
		// Check if any user with this email
		
		String queryS = "SELECT s FROM User s WHERE s.userEmail = '"+email+"'";
		
		Query query = em.createQuery(queryS);
		
		List <User> userList = query.getResultList();
		
		if(userList.isEmpty()){
			User user = new User();
			
			@SuppressWarnings("deprecation")
			Date aux = new Date(1970, 01, 01);
			
			user.setUserEmail(email);
			user.setUserName(name);
			user.setUserSurname(surname);
			user.setUserPassword(password);
			user.setUserBirthdate(aux);
			
			em.persist(user);
			em.merge(user);
			
			return 1;
		}
		
		return 2;		
	}

}
