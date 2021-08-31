package com.cognixia.jump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.ToDo;
import com.cognixia.jump.repository.ToDoRepository;

@RequestMapping("/api")
@RestController
public class ToDoController {
	
	@Autowired
	ToDoRepository repo;
	
	@GetMapping("/todo")
	public List<ToDo> getToDos() {
		return repo.findAll();
	}
	
	
	
	
}
