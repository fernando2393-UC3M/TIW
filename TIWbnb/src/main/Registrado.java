package main;

import model.User;

public class Registrado {
	
	// Here we obtain registered user information and post it into the information form
	
	// It is needed a way of data storage of introduced user to get its information from DB
	
	User user = new User();
	
	String email = user.getUserEmail();
	String name = user.getUserName();
	String surname = user.getUserSurname();
	String password = user.getUserPassword();
	String birthdate = user.getUserBirthdate();

}
