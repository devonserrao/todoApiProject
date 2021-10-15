package com.cognixia.jump.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.ToDo;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.ToDoRepository;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@RestController
public class ToDoController {
	
	@Autowired
	ToDoRepository repo;
	
	@GetMapping("/todo")
	public List<ToDo> getToDos() {
		return repo.findAll();
	}
	
	@ApiOperation(value = "Deletes a todo from the database. Must provide ID of todo in the URI",
					notes = " Since Cascade is active, will delete todo from the User todos list as well.")
	@DeleteMapping("/todo/{id}")
	public ResponseEntity<?> deleteToDo(@PathVariable int id) {
		ToDo deleted = new ToDo();
		
		if(repo.existsById(id)) {
			deleted = repo.getById(id);
			repo.deleteById(id);
			
//			System.out.println(deleted.getDueDate());
		}
		
		if(deleted.getId() == -1) {
			return ResponseEntity.status(404)
					.body("ToDo not found!");
		}
		
		return ResponseEntity.status(200)
						.body("ToDo deleted with id = " + deleted.getId());

	}
	
	// Add a todo and link to a User id -> cant input user id this way
	@ApiOperation(value = "Add a todo and link it to a User. Must provide ID of User within the ToDo object")
	@PostMapping("/todo")
	public ResponseEntity<?> addToDo(@RequestBody ToDo todo) {
		todo.setId(-1);
		
		ToDo added = repo.save(todo);
		
		return ResponseEntity.status(201).body(added);
	}
	
	
	// Update a todo to completed [finished = true]
	@ApiOperation(value = "Updates a todo's completed field to True to denote completion of Todo task.",
						notes = "Must provide ID of User within the ToDo object")
	@PatchMapping("/todo/finished/{id}")
	public ResponseEntity<?> updateToDo(@PathVariable int id) {
		
		Optional<ToDo> found = repo.findById(id);
		
		if(found.isPresent()) {
			ToDo toFinish = found.get();
			toFinish.setFinished(true);
			
			ToDo updated = repo.save(toFinish);
			return ResponseEntity.status(200).body(updated);			
		}
		
		
		return ResponseEntity.status(404)
								.body("ToDo with id = " + id + "not found and cannot update to Finished!");
		
	}
	
	/*
	 *  API call to update a ToDo's dueDate
	 *  	==> Need to put a Map object in RequestBody to push in Date
	 */
	@PatchMapping("/todo/duedate")
	public ResponseEntity<?> updateDueDate(@RequestBody Map<String, String> data) {
		
		int id = Integer.parseInt(data.get("id"));
		LocalDate dueDate = LocalDate.parse(data.get("dueDate"));
				
		Optional<ToDo> found = repo.findById(id);
		
		if(found.isPresent()) {
			ToDo toUpdateDueDate = found.get();
						
			toUpdateDueDate.setDueDate(dueDate);
			
			ToDo updated = repo.save(toUpdateDueDate);
			return ResponseEntity.status(200).body(updated);			
		}
		
		
		return ResponseEntity.status(404)
								.body("ToDo with id = " + id + "not found and cannot update DueDate!");
		
	}
	
	/*
	 *  API call to delete all ToDos of a User
	 *  	==> Need to pass a User object in the Request Body
	 *  	==> Can try passing just user id in the URL instead
	 */
	@DeleteMapping("/todo/user")
	public ResponseEntity<?> deleteToDosOfUser(@RequestBody User user) {
		repo.removeByUser(user);
		
		return ResponseEntity.status(200).body("Todos of User with id = " + user.getId() + " was DELETED!");
	}
	
	
	
}
