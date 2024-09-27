package com.example.demo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

public class TestUtils {

	public static void injectObjects(Object target, String fieldName, Object toInject) {
		boolean wasPrivate = false;
		try {
			Field f = target.getClass().getDeclaredField(fieldName);

			if (!f.isAccessible()) {
				f.setAccessible(true);
				wasPrivate = true;
			}
			f.set(target, toInject);

			if (wasPrivate) {
				f.setAccessible(false);
			}

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static User createUser() {
		User user = new User();
		user.setId(1L);
		user.setUsername("test");
		user.setPassWord("12345678");
		user.setCart(createCart(user));
		return user;
	}

	public static Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setId(1L);
		List<Item> items = createItems();
		cart.setItems(createItems());
		cart.setTotal(items.stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());
		cart.setUser(user);
		return cart;
	}

	public static List<Item> createItems() {
		List<Item> items = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			items.add(createItem(i));
		}
		return items;
	}

	public static Item createItem(long id) {
		Item item = new Item();
		item.setId(id);
		item.setPrice(BigDecimal.valueOf(id * 2));
		item.setName("Item " + item.getId());
		item.setDescription("abcd ");
		return item;
	}

	public static List<UserOrder> createOrders() {
		List<UserOrder> orders = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			UserOrder order = new UserOrder();
			Cart cart = createCart(createUser());
			order.setItems(cart.getItems());
			order.setTotal(cart.getTotal());
			order.setUser(createUser());
			order.setId(Long.valueOf(i));
			orders.add(order);
		}
		return orders;
	}
}
