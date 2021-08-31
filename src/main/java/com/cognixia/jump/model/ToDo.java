package com.cognixia.jump.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Makes sure our data loads fast enough without getting error of unable to serialize fast enough!
public class ToDo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Column(columnDefinition = "varchar(255) default 'Missing Description!'")
	private String description;
	
	@NotNull
	private Boolean finished;
	
	@NotNull
	private LocalDate dueDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public ToDo() {
		this(-1, "Missing Description!", false);
	}
	
	public ToDo(Integer id, @NotBlank String description, @NotNull Boolean finished) {
		super();
		this.id = id;
		this.description = description;
		this.finished = finished;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	// Only need Setter for the Users
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ToDo [id=" + id + ", description=" + description + ", finished=" + finished + ", dueDate = " + dueDate + " , user=" + user + "]";
	}
	
	
	
	
}
