package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.*;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class TestUserController {

	private UserController userController;
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private CartRepository cartRepository = Mockito.mock(CartRepository.class);
	private BCryptPasswordEncoder enCoder = Mockito.mock(BCryptPasswordEncoder.class);

	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.injectObjects(userController, "userRepository", userRepository);
		TestUtils.injectObjects(userController, "cartRepository", cartRepository);
		TestUtils.injectObjects(userController, "bCryptPasswordEncoder", enCoder);

	}

	@Test
	public void createUserOK() {
		Mockito.when(enCoder.encode("12345678")).thenReturn("thisIsHashed");
		CreateUserRequest user = new CreateUserRequest();
		user.setUsername("test");
		user.setPassWord("12345678");
		user.setConfirmPassWord("12345678");

		ResponseEntity<User> res = userController.createUser(user);

		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		User ur = res.getBody();

		assertNotNull(ur);
		assertEquals(0, ur.getId());
		assertEquals("test", ur.getUsername());
		assertEquals("thisIsHashed", ur.getPassWord());

	}

	@Test
	public void findUserId() {
		long id = 1L;
		User user = new User();
		user.setUsername("test");
		user.setPassWord("12345678");
		user.setId(id);

		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		ResponseEntity<User> res = userController.findById(id);

		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		User ur = res.getBody();
		assertNotNull(ur);
		assertEquals(id, ur.getId());
		assertEquals("test", ur.getUsername());
		assertEquals("12345678", ur.getPassWord());
	}

	@Test
	public void findUserName() {
		long id = 1L;
		User user = new User();
		user.setUsername("test");
		user.setPassWord("12345678");
		user.setId(id);

		Mockito.when(userRepository.findByUsername("test")).thenReturn(user);

		ResponseEntity<User> res = userController.findByUserName("test");
		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		User ur = res.getBody();
		assertNotNull(ur);
		assertEquals(id, ur.getId());
		assertEquals("test", ur.getUsername());
		assertEquals("12345678", ur.getPassWord());
	}
	
	@Test
	public void findUserNameNOK() {
		ResponseEntity<User> response = userController.findByUserName("Username");

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void createUserNOK() {
		Mockito.when(enCoder.encode("123456")).thenReturn("thisIsHashed");
		CreateUserRequest user = new CreateUserRequest();
		user.setUsername("test");
		user.setPassWord("123456");
		user.setConfirmPassWord("123456");

		ResponseEntity<User> res = userController.createUser(user);
		assertNotNull(res);
		assertEquals(400, res.getStatusCodeValue());
	}

}
