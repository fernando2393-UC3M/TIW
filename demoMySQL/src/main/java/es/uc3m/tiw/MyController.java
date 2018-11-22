package es.uc3m.tiw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uc3m.tiw.domains.Address;
import es.uc3m.tiw.domains.AddressDAO;
import es.uc3m.tiw.domains.User;
import es.uc3m.tiw.domains.UserDAO;

@Controller
@CrossOrigin
public class MyController {

	@Autowired
	UserDAO daoUs;
	
	@Autowired
	AddressDAO daoAd;

	@RequestMapping("/users")
	public @ResponseBody List<User> getUsers(){
		return daoUs.findAll();
	}
	
	@RequestMapping("/users/{name}")
	public @ResponseBody List<User> getUserByName(@PathVariable String name){
		return daoUs.findByName(name);
	}
	
	@RequestMapping("/users/{name}/{surname}")
	public @ResponseBody List<User> getUserByNameAndSurname(@PathVariable String name,
															@PathVariable String surname){
		return daoUs.findByNameAndSurname(name, surname);
	}
	
	@RequestMapping("/userstreet/{street}")
	public @ResponseBody List<User> getUserByStreet(@PathVariable String street){
		return daoUs.findByAddressStreet(street);
	}

	@RequestMapping("/address/{streetName}")
	public @ResponseBody List<Address> getAddressByStreet(@PathVariable String streetName){
		return daoAd.findByStreet(streetName);
	}

	@RequestMapping(method = RequestMethod.POST, value="/user")
	public @ResponseBody User saveUser(@PathVariable @Validated User puser){
		return daoUs.save(puser);
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/user/{id}")
	public @ResponseBody void deleteUser(@PathVariable @Validated Long id){
		User us = daoUs.findById(id).orElse(null);
		if (us != null){
			daoUs.delete(us);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value="/user/{id}")
	public @ResponseBody User updateUser(@PathVariable @Validated Long id, @RequestBody User pUser){
		User us = daoUs.findById(id).orElse(null);
		us.setName(pUser.getName());
		us.setSurname(pUser.getSurname());
		return daoUs.save(us);
	}
	
	
	
	
}
