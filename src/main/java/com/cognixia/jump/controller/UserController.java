package com.cognixia.jump.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UserLoginFailedException;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	UserRepository repo;
	
	@GetMapping("/user")
	public List<User> getUsers() {
		return repo.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUser(@PathVariable int id) throws ResourceNotFoundException {
		if(repo.existsById(id)) {
			return repo.getById(id);
		}
		
		// If user not found -> Return ResourceNotFoundException
		throw new ResourceNotFoundException("User with id = " + id + " not found.");
			
	}
	
	/*
	 *  API call to check if username and password combination are found in db!
	 */
	@GetMapping("/user/login")
	public User loginUser(@PathParam(value = "username") String username, @PathParam(value = "password") String password) throws UserLoginFailedException {
	
		if(repo.loginUser(username, password) != null)
			return repo.loginUser(username, password);
		
		throw new UserLoginFailedException("No user found with username = " + username + " and password = " + password + ".");
		// Issue with this Exception => Its showing the full stacktrace rather than just Error details!
	}
	
	
	
}
