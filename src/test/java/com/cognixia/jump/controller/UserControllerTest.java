package com.cognixia.jump.controller;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UserLoginFailedException;
import com.cognixia.jump.model.ToDo;
import com.cognixia.jump.model.User;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

private final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserController controller;
	
	@Test
	void testGetAllUsers() throws Exception {
		
		String uri = STARTING_URI + "/user";
		
		List<User> allUsers = Arrays.asList(
					new User(1, "Devon Serrao", "devon@gmail.com", "ToastisGood", new ArrayList<ToDo>() ),
					new User(2, "Darren", "darren@gmail.com", "test123", new ArrayList<ToDo>() ),
					new User(3, "Testing", "test@gmail.com", "Test12323231", new ArrayList<ToDo>() ),
					new User(4, "Denver", "denver@gmail.com", "Test12323231", new ArrayList<ToDo>() )
				);
		
		when(controller.getUsers()).thenReturn(allUsers);
		
		mockMvc.perform(get(uri))
				.andDo( print() )
				.andExpect(status().isOk());
		
	}
	
	@Test
	void testGetUser() throws Exception {
		
		int id = 2;
		String uri = STARTING_URI + "/user/{id}";
		
		User userFound = new User(2, "Darren", "darren@gmail.com", "test123", new ArrayList<ToDo>() );
		
		when(controller.getUser(id)).thenReturn(userFound);
		
		mockMvc.perform( get(uri, id) )
				.andDo( print() )
				.andExpect( status().isOk() )
				.andExpect( jsonPath("$.id").value(userFound.getId()) )
				.andExpect( jsonPath("$.name").value(userFound.getName()) )
				.andExpect( jsonPath("$.username").value(userFound.getUsername()) )
				.andExpect( jsonPath("$.password").value(userFound.getPassword()) )
				.andExpect( jsonPath("$.todos").value(userFound.getTodos()) );
		
	}
	
	@Test
	void testGetUserNotFound() throws Exception {
		
		int id = 1000;
		String uri = STARTING_URI + "/user/{id}";
		
		when(controller.getUser(id))
				.thenThrow(new ResourceNotFoundException("User with id = " + id + " not found."));
		
		mockMvc.perform( get(uri, id) )
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
	}
	
	@Test
	void testLoginUser() throws Exception {
		
		String validUsername = "darren@gmail.com";
		String validPassword = "test123";
		
		String uri = STARTING_URI + "/user/login?username=" + validUsername + "&password=" + validPassword;
		
		User userFound = new User(2, "Darren", "darren@gmail.com", "test123", new ArrayList<ToDo>() );
		
		when(controller.loginUser(validUsername, validPassword)).thenReturn(userFound);
		
		mockMvc.perform( get(uri) )
				.andDo( print() )
				.andExpect( status().isOk() )
				.andExpect( jsonPath("$.id").value(userFound.getId()) )
				.andExpect( jsonPath("$.name").value(userFound.getName()) )
				.andExpect( jsonPath("$.username").value(userFound.getUsername()) )
				.andExpect( jsonPath("$.password").value(userFound.getPassword()) )
				.andExpect( jsonPath("$.todos").value(userFound.getTodos()) );
		
	}
	
	@Test 
	void testLoginInvalidUser() throws Exception {
		
		String invalidUsername = "invalid_user12@gmail.com";
		String invalidPassword = "sdmam0212837";
		
		String uri = STARTING_URI + "/user/login?username=" + invalidUsername + "&password=" + invalidPassword;
		
		when(controller.loginUser(invalidUsername, invalidPassword))
				.thenThrow(new UserLoginFailedException("No user found with username = " + invalidUsername + " and password = " + invalidPassword + "."));
		
		mockMvc.perform( get(uri) )
				.andDo( print() )
				.andExpect( status().isNotFound() );
		
	}
	
	
	
// TODO	
//	@Test
//	void testCreateUser() throws Exception {
//		
//		String uri = STARTING_URI + "/user";
//		
//		User userToCreateTest = new User(-1, "TESTING USER", "TESTING EMAIL", "TESTPassword!223", new ArrayList<ToDo>() );
//		
//		when( controller.addUser( Mockito.any(User.class) )).thenReturn(userToCreateTest);
//		
//		String userJson = "{\"id\" : " + userToCreateTest.getId() 
//				+ ", \"name\" : \"" + userToCreateTest.getName() 
//				+ "\"" + ", \"username\" : \"" + userToCreateTest.getUsername() 
//				+ "\"" + ", \"password\" : \"" + userToCreateTest.getPassword()
//				+ "\"" + ", \"todos\" : " + userToCreateTest.getTodos()
//			    + "}";;
//				
//		mockMvc.perform( post(uri)
//							.content( userJson )
//							.contentType(MediaType.APPLICATION_JSON_VALUE) )
//				.andDo(print())
//				.andExpect( status().isCreated() )
//				.andExpect( content().contentType(MediaType.APPLICATION_JSON_VALUE) );	
//
//	}
	
	// TODO Produces assertionError - > returns 400 instead of 200.
//	@Test
//	void testAddToDoForUser() throws Exception {
//		
//		int id = 1;
//		String uri = STARTING_URI + "/user/{id}/todo";
//		
//		User updatedUserWithAddedToDo = new User(1, "Devon Serrao", "devon@gmail.com", "ToastisGood", new ArrayList<ToDo>() );
//		ToDo toDoToAppend = new ToDo(-1, "Test Description", false);
//		
//				
//		when(controller.addToDoForUser(id, toDoToAppend)).thenReturn(updatedUserWithAddedToDo);
//		
//		mockMvc.perform( put(uri, id) )
//				.andDo(print())
//				.andExpect( status().isOk());
//		
//	}

	@Test
	void testUpdateUsername() throws Exception {
		
		int id = 1;
		String usernameChanged = "devon123TEST@gmail.com";
		
		String uri = STARTING_URI + "/user/username?id=" + id + "&username=" + usernameChanged;

		User updatedUser = new User(1, "Devon Serrao", "devon123TEST@gmail.com", "ToastisGood", new ArrayList<ToDo>() );
		
		when(controller.updateUsername(id, usernameChanged)).thenReturn(updatedUser);
		
		mockMvc.perform( patch(uri) )
				.andDo( print() )
				.andExpect( status().isOk() );
	}
	
}
