package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.*;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static com.example.demo.TestUtils.*;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class TestCartController {

	private CartController cartController;

	private UserRepository userRepository = Mockito.mock(UserRepository.class);
	private CartRepository cartRepository = Mockito.mock(CartRepository.class);
	private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

	@Before
	public void setUp() {
		cartController = new CartController();
		TestUtils.injectObjects(cartController, "userRepository", userRepository);
		TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

		Mockito.when(userRepository.findByUsername("test")).thenReturn(createUser());
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(createItem(1)));
	}

	@Test
	public void addTocartOK() {

		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("test");
		req.setItemId(1);
		req.setQuantity(2);

		ResponseEntity<Cart> res = cartController.addTocart(req);

		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		Cart cart = res.getBody();

		assertNotNull(cart);
		assertEquals("test", cart.getUser().getUsername());
		assertEquals(4, cart.getItems().size());
		assertEquals(createItem(1), cart.getItems().get(0));
		assertEquals(new BigDecimal(10), cart.getTotal());

	}

	@Test
	public void addTocartNOK() {
		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("NotFound");
		req.setItemId(1);
		req.setQuantity(1);

		ResponseEntity<Cart> res = cartController.addTocart(req);

		assertNotNull(res);
		assertEquals(404, res.getStatusCodeValue());
	}

	@Test
	public void removeFromCartOK() {

		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("test");
		req.setQuantity(1);
		req.setItemId(1);

		ResponseEntity<Cart> res = cartController.removeFromcart(req);
		assertNotNull(res);
		assertEquals(200, res.getStatusCodeValue());

		Cart cart = res.getBody();

		assertNotNull(cart);
		assertEquals("test", cart.getUser().getUsername());
		assertEquals(1, cart.getItems().size());
		assertEquals(createItem(2), cart.getItems().get(0));
		assertEquals(new BigDecimal(4), cart.getTotal());

	}

	@Test
	public void removeFromCartNOK() {
		ModifyCartRequest req = new ModifyCartRequest();
		req.setUsername("NotFound");
		req.setItemId(9);
		req.setQuantity(1);

		ResponseEntity<Cart> res = cartController.removeFromcart(req);

		assertNotNull(res);
		assertEquals(404, res.getStatusCodeValue());
	}
}
