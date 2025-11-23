package com.url.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortener.dtos.RegisterRequest;
import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.models.User;
import com.url.shortener.service.UserService;


import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 	api/auth same end point in custom security config because this is where all are authentication related 
	endpoint register, login, forgotpass reside. 
	and we have made permitAll(allowed) to this url (in our webSecurityconfig )  without authentication because 
	Like If we want to login it has to be a public page so we need to make it public so pepole come and login register.
 */
@RestController
@RequestMapping("/api/auth") 
@AllArgsConstructor
public class AuthController {

	@Autowired
	private UserService userService;
	
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	/*
	 * MEthod : This method will help us to register User into our application
	 * NOTE: So what will user will send here in this register so whenever we calling this /public/register endpoint 
	 * what information do we need to send here. soooo For that we need to create a DTO of type register request and we accept the 
	 * instance of registerRequest as per request body.
	 */
	@PostMapping("/public/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){  //@RequestBody so it come here as the req body
        logger.info("In Register action, username = {}", registerRequest.getUsername());
		//Creating an instance of user
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(registerRequest.getPassword());
		user.setEmail(registerRequest.getEmail());
		user.setRole("ROLE_USER");
        logger.info("In Register action, username = {}", user.getUsername());
		userService.registerUser(user);
		return ResponseEntity.ok("User Registered Successfully");
	}

	
	
	/*
	 * @Method : This method is used for login
	 */
	@PostMapping("/public/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
		System.out.println("loginRequest-> "+ loginRequest);

		//Now we need to authenticate the user so for that we are going to make use of our service class i.e userService. 
		
		return ResponseEntity.ok(userService.authenticateUser(loginRequest));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
