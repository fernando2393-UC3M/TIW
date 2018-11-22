package es.uc3m.tiw.domains;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {

	public List<User> findByName(String name);
	public List<User> findByNameAndSurname(String name, String surname);
	public List<User> findByAddressStreet(String streetname);
	public List<User> findAll();
	
}
