package com.cognixia.jump.controller;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
		
		String uri = STARTING_URI + "/user/{id}";
		int id = 2;
		
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
}
