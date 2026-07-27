package com.umfrancisco.shoppingcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.umfrancisco.shoppingcart.config.JwtProvider;
import com.umfrancisco.shoppingcart.config.UserSession;
import com.umfrancisco.shoppingcart.model.User;
import com.umfrancisco.shoppingcart.payload.AuthResponse;
import com.umfrancisco.shoppingcart.payload.UserRequest;
import com.umfrancisco.shoppingcart.repository.UserRepository;
import com.umfrancisco.shoppingcart.service.UserServiceImpl;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:3000")
public class UserController {
	
	private UserRepository repository;
	private PasswordEncoder passwordEncoder;
	private UserServiceImpl userService;
	
	public UserController(UserRepository repository, PasswordEncoder passwordEncoder, UserServiceImpl userService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}
	
	@GetMapping("/hello")
	public String hello() {
		return UserSession.name;
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout() {
		UserSession.ID = -1L;
		UserSession.name = "";
		return ResponseEntity.status(HttpStatus.OK).body("Logout");
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> signin(@RequestBody UserRequest user) {
		Authentication auth = authenticate(user.username(), user.password());
		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = JwtProvider.generateToken(auth);
//		AuthResponse response = new AuthResponse(token);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = userService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username and password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
		User isExistingUser = repository.findByUsername(user.getUsername());
		if (isExistingUser != null) {
			return null;
		}
		User newUser = new User(
				user.getEmail(),
				user.getUsername(),
				passwordEncoder.encode(user.getPassword()), 
				user.getRole());
		User savedUser = repository.save(newUser);
		UserSession.ID = savedUser.getUserCode();
		UserSession.name = savedUser.getUsername();
		
		Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = JwtProvider.generateToken(auth);
		
		AuthResponse response = new AuthResponse(token);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
