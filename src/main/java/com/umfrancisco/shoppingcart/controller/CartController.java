package com.umfrancisco.shoppingcart.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.umfrancisco.shoppingcart.config.UserSession;
import com.umfrancisco.shoppingcart.model.Cart;
import com.umfrancisco.shoppingcart.payload.ProductRequest;
import com.umfrancisco.shoppingcart.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins="http://localhost:3000")
public class CartController {
	
	private final CartService service;
	
	public CartController(CartService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<Cart> findAll() {
		return service.findAll();
	}
	
	@GetMapping("/customer")
	public List<Cart> findByCustomer() {
		return service.findByUserCode(UserSession.ID);
	}
	
	@PostMapping
	public Cart save(@RequestBody List<ProductRequest> requests) {
		return service.saveAll(requests);
	}
	
}
