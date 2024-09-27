package com.example.demo.controllers;

import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static com.example.demo.TestUtils.*;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class TestOrderController {

	private OrderController orderController;
	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

	@Before
	public void setUp() {
		orderController = new OrderController();
		TestUtils.injectObjects(orderController, "userRepository", userRepository);
		TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

		Mockito.when(userRepository.findByUsername("test")).thenReturn(createUser());
		Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(createOrders());
	}

	@Test
	public void submitOK() {
		ResponseEntity<UserOrder> res = orderController.submit("test");
		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		UserOrder order = res.getBody();
		assertEquals(createItems(), order.getItems());
		assertEquals(createUser().getId(), order.getUser().getId());

	}

	@Test
	public void submitNOK() {
		ResponseEntity<UserOrder> res = orderController.submit("NOK");
		assertNotNull(res);
		assertEquals(404, res.getStatusCodeValue());
		assertNull(res.getBody());
	}

	@Test
	public void getOrdersForUserOK() {
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<UserOrder> orders = response.getBody();
		assertEquals(2, orders.size());
	}
}
