package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.*;

public class TestItemController {

	private ItemController itemController;

	private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

	@Before
	public void setup() {
		itemController = new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
		when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
		when(itemRepository.findAll()).thenReturn(createItems());
		when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));
	}

	@Test
	public void getItemsOK() {
		ResponseEntity<List<Item>> response = itemController.getItems();

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();

		assertEquals(createItems(), items);
	}

	@Test
	public void getItemByIdOK() {

		ResponseEntity<Item> response = itemController.getItemById(1L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());

		Item item = response.getBody();
		assertEquals(createItem(1L), item);
	}

	@Test
	public void verify_getItemByNameInvalid() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid name");

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
		assertNull(response.getBody());
	}
}
