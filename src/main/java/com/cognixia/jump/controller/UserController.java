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

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	UserRepository repo;
	
	@Autowired
	ToDoRepository toDoRepo;
	
	@ApiOperation(value = "Returns a list of users from the database with all their details.")
	@GetMapping("/user")
	public List<User> getUsers() {
		return repo.findAll();
	}
	
	@ApiOperation(value = "Retrieves a User corresponding to an ID.",
			notes = "Pass in User ID in the URL || If user with ID not found, will throw ResourceNotFoundException")
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
	@ApiOperation(value = "Retrieves a User corresponding to username and password input.",
			notes = "Pass in Username & Password in the URL path parameters || If username & password match not found, will throw UserLoginFailedException")
	@GetMapping("/user/login")
	public User loginUser(@PathParam(value = "username") String username, @PathParam(value = "password") String password) throws UserLoginFailedException {
	
		if(repo.loginUser(username, password) != null)
			return repo.loginUser(username, password);
		
		throw new UserLoginFailedException("No user found with username = " + username + " and password = " + password + ".");
	}
	
	/*
	 *  API call to update Username of a User
	 */
	@ApiOperation(value = "Updates an existing User's username identified by input ID.",
			notes = "Pass in Username and User ID in the URL path parameters || If user with ID not found, will throw ResourceNotFoundException")
	@PatchMapping("user/username")
	public User updateUsername(@PathParam(value = "id") int id, @PathParam(value = "username") String username) throws ResourceNotFoundException{
		
		Optional<User> found = repo.findById(id);
		User toUpdate, updated;
		
		if(found.isPresent()) {
			repo.updateUsername(id, username);
			updated = repo.getById(id);
			return updated;
			
		}
		
		throw new ResourceNotFoundException("User with id = " + id + " couldnt be found to update the username!");

	}
	
	/*
	 *  API call to update Password of a User
	 */
	@ApiOperation(value = "Updates an existing User's password identified by input ID.",
			notes = "Pass in Password and User ID in the URL path parameters || If user with ID not found, will throw ResourceNotFoundException")
	@PatchMapping("user/password")
	public ResponseEntity<?> updatePassword(@PathParam(value = "id") int id, @PathParam(value = "password") String password) {
		
		Optional<User> found = repo.findById(id);
		User toUpdate, updated;
		
		if(found.isPresent()) {
			repo.updatePassword(id, password);
			return ResponseEntity.status(200).body("User of id = " + id + " PASSWORD was updated!");
			
		}
		else
			return ResponseEntity.status(404)
					.body("User with id = " + id + " couldnt be found to update the password!");

	}
	
	/*
	 *  API call to add a new User with potentially new ToDos
	 */
	@ApiOperation(value = "Creates a new User with potentially new todos on creation of User.",
			notes = "Pass in User object in the Request Body. || Will ensure all todos entered are created as well.")
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
		user.setId(-1);
		
		// Ensure each ToDo attached to the new User 
		//			-> makes a new ToDo instead of accidentally stealing an existing ToDo from a User
		// 				Eg: (User has todo_id = 1 ==> But todo_id = 1 already exists for another user 
		//							===> will delete from old user and add to new one instead)
		// 	To fix the issue:
		//			==> setId for each ToDo to -1 while inserting new User
		for(ToDo todo : user.getTodos()) {
			todo.setId(-1);
		}
		
		User added = repo.save(user);
		return ResponseEntity.status(201).body(added);
	}
	
	/*
	 *  API call to get the list of Todos of a User with id
	 */
	@ApiOperation(value = "Retrieves the list of Todos of a User with ID",
			notes = "Pass in User ID in the URL followed by /todos || If User with ID not found, will return ResourceNotFoundException")
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
	
	/*
	 *  API call to add a ToDo and link it to the User with id
	 */
	@ApiOperation(value = "Adds a Todo and links it to a User with ID given",
			notes = "Pass in User ID in the URL followed by /todo & a ToDo object in the Request Body || If User with ID not found, will return ResourceNotFoundException")
	@PutMapping("/user/{id}/todo")
	public User addToDoForUser(@PathVariable int id, @RequestBody ToDo todo) throws ResourceNotFoundException {
		
		todo.setId(-1);
		ToDo addedToDo = toDoRepo.save(todo);
		
		Optional<User> found = repo.findById(id);
		
		if(found.isPresent()) {
			User toUpdate = found.get();
			toUpdate.addTodo(addedToDo);
			
			User updated = repo.save(toUpdate);
			return updated;
		}
		
		throw new ResourceNotFoundException("User with id = " + id + " not found to UPDATE!.");
	}
	
	/* 
	 * Delete all todos for a single user
	 * 			- Issue: Passes but doesnt actually delete from db!
	 * 			- ###################################
	 * 			  # May need to comment this out!!! #
	 * 			  ###################################
	 * */
//	@PutMapping("/user/{id}/empty")
//	public ResponseEntity<?> deleteToDosForUser(@PathVariable int id) {
//		
//		Optional<User> found = repo.findById(id);
//		
//		if(found.isPresent()) {
//			User toRemoveToDos = found.get();
//			List<ToDo> toDosToClear = toRemoveToDos.getTodos();
//			
//			for(int i = 0; i < toDosToClear.size(); i++ ) {
//				toDoRepo.deleteById(toDosToClear.get(i).getId());				
//			}
//				
//			toRemoveToDos.deleteAllToDos();
//			
//			User updated = repo.save(toRemoveToDos);
//			return ResponseEntity.status(200).body(updated);
//		}
//		
//		return ResponseEntity.status(404)
//								.body("Couldnt find user with id = " + id + " to remove all ToDos.");
//	}
	
	/*
	 *  API call to finish each Todo of a User [finished = true]
	 */
	@ApiOperation(value = "Completes every Todo linked to a User with ID",
			notes = "Pass in User ID in the URL followed by /finish || If User with ID not found, will return ResourceNotFoundException")
	@PutMapping("/user/{id}/finish")
	public ResponseEntity<?> finishAllToDoForUser(@PathVariable int id) {
		
		Optional<User> found = repo.findById(id);
		
		if(found.isPresent()) {
			User toUpdate = found.get();
			List<ToDo> toDosToUpdate = toUpdate.getTodos();
			
			for(int i = 0; i < toDosToUpdate.size(); i++) {
				ToDo todoOfUser = toDoRepo.getById(toDosToUpdate.get(i).getId());
				todoOfUser.setFinished(true);
			}
			
			User updated = repo.save(toUpdate);
			return ResponseEntity.status(200).body(updated);
		}
		
		return ResponseEntity.status(404)
								.body("Couldnt find user with id = " + id + " to add a ToDo for.");
	}
	
}
