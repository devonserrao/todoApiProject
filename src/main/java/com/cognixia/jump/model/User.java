package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Makes sure our data loads fast enough without getting error of unable to serialize fast enough!
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	@Column(columnDefinition = "varchar(255) default 'Unknown User Entered!'")
	private String username;
	
	// At least 8 characters - Try out other patterns later
	@Pattern(regexp = "^[A-Za-z0-9]\\w{8,}$")
	@Column(columnDefinition = "varchar(50) default 'XXXXXXXXX' ")
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ToDo> todos;

	public User() {
		this(-1, "N/A", "Unknown User Entered!", "XXXXXXXXX", new ArrayList<ToDo>());
	}
	
	public User(Integer id, @NotBlank String name, @NotBlank String username,
			@Pattern(regexp = "^[A-Za-z0-9]\\w{8,}$") String password, List<ToDo> todos) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.todos = todos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ToDo> getTodos() {
		return todos;
	}

	public void setTodos(List<ToDo> todos) {

		for(int todo = 0; todo < todos.size(); todo++) {
			addTodo(todos.get(todo));
		}
	}
	
	public void addTodo(ToDo todo) {
		todo.setUser(this);
		todos.add(todo);
	}
	
	public void deleteAllToDos() {
		todos.clear();
	}
	
	
	
	
}
