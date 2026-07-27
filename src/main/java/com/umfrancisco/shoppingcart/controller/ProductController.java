package com.umfrancisco.shoppingcart.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.umfrancisco.shoppingcart.payload.ProductDTO;
import com.umfrancisco.shoppingcart.service.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:3000")
public class ProductController {
	
	private final ProductService service;
	
	public ProductController(ProductService service) {
		this.service = service;
	}
	
	@GetMapping("/public/product")
	public ResponseEntity<List<ProductDTO>> getProducts() {
		return ResponseEntity.status(HttpStatus.OK).body(service.getProducts());
	}
	
	@GetMapping("/public/product/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.getProductById(id));
	}
	
	@GetMapping("/public/product/category/{category}")
	public ResponseEntity<List<ProductDTO>> getProductByCategory(@PathVariable String category) {
		return ResponseEntity.status(HttpStatus.OK).body(service.getProductByCategory(category));
	}
	
	@GetMapping("/public/product/highlight/first")
	public ResponseEntity<ProductDTO> getHighlightProduct() {
		return ResponseEntity.status(HttpStatus.OK).body(service.getHighlightedProduct());
	}
	
	@GetMapping("/public/product/highlight/all")
	public ResponseEntity<List<ProductDTO>> getHighlightProducts() {
		return ResponseEntity.status(HttpStatus.OK).body(service.getHighlightedProducts());
	}
	
	@PostMapping("/admin/product")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
		ProductDTO savedProduct = service.addProduct(productDTO);
		return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
	}
	
	@PutMapping("/admin/product/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
		ProductDTO updatedProduct = service.updateProduct(productDTO, id);
		return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);	
	}
	
	@DeleteMapping("/admin/product/{id}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
		ProductDTO existingProduct = service.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.OK).body(existingProduct);
	}
	
}
