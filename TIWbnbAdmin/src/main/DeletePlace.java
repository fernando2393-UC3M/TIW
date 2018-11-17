package main;

import javax.persistence.EntityManager;

import model.Home;

public class DeletePlace {
	
	public void delete(EntityManager em, int id) {
		
		Home place = em.find(Home.class, id); // Look for the place by id
		
		em.remove(place); // Remove place from database
		
	}
}
