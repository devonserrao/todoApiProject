package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UserLoginFailedException;
import com.cognixia.jump.model.ToDo;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.ToDoRepository;
import com.cognixia.jump.repository.UserRepository;

@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	UserRepository repo;
	
	@Autowired
	ToDoRepository toDoRepo;
	
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
	
	
	// Issue with updating username & password!
//	@PatchMapping("user/username")
//	public ResponseEntity<?> updateUsername(@PathParam(value = "id") int id, @PathParam(value = "username") String username) {
//		
//		Optional<User> found = repo.findById(id);
//		User toUpdate, updated;
//		
//		if(found.isPresent()) {
//			toUpdate = found.get();
//			toUpdate.setUsername(username);
//			
//			updated = repo.save(toUpdate);
//			
//		}
//		else
//			updated = new User();
//		
//		if(updated.getId() == -1) {
//			return ResponseEntity.status(404)
//									.body("User with id = " + id + " couldnt be found to update the username!");
//		}
//		
//		return ResponseEntity.status(200).body(updated);
//	}
	
	
	// This might have updated the ToDo table by accident 
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
		user.setId(-1);
		
		User added = repo.save(user);
		return ResponseEntity.status(201).body(added);
	}
	
	// Returns list of Todos of User with id
	@GetMapping("/user/{id}/todos")
	public ResponseEntity<?> getTodosOfUser(@PathVariable int id) {
		Optional<User> found = repo.findById(id);
		List<ToDo> todosOfUser;
		
		if(found.isPresent()) {
			todosOfUser = found.get().getTodos();
			return ResponseEntity.status(200).body(todosOfUser);
		}
		
		return ResponseEntity.status(404).body("User not found with id = " + id);
		
	}
	
	// Adds a ToDo and links it to the User with id
	@PutMapping("/user/{id}/todo")
	public ResponseEntity<?> addToDoForUser(@PathVariable int id, @RequestBody ToDo todo) {
		
		todo.setId(-1);
		ToDo addedToDo = toDoRepo.save(todo);
		
		Optional<User> found = repo.findById(id);
		
		if(found.isPresent()) {
			User toUpdate = found.get();
			toUpdate.addTodo(addedToDo);
			
			User updated = repo.save(toUpdate);
			return ResponseEntity.status(200).body(updated);
		}
		
		return ResponseEntity.status(404)
								.body("Couldnt find user with id = " + id + " to add a ToDo for.");
	}
	
	
	
}