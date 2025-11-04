package com.url.shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.models.User;
import com.url.shortener.repository.UserRepository;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.security.jwt.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	// initialling these using @AllArgsConstructor we can also use Autowired
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	
	/**
	 * Method: for authenticating user for login
	 * JwtAuthenticationResponse is the class we have already created  and it represents the token 
	 */
	public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
		//First we create Authentication object for that we need AuthenticationManager 
		//First Step :: UsernamePasswordAuthenticationToken is a class provided by spring security and this is a representation to 
		//present the username and password (so this is basically representing the credentials) and then the 
		//instance of of UsernamePasswordAuthenticationToken passed to authenticate method which is coming from authenticationManager
		//And authenticationManager is use to process an authentication request 
		
		/**
		 * Authentication Part
		 * If the username and password are valid a fully populated authentication object will be returned without any error
		 */
		Authentication authentication = authenticationManager.authenticate(
				
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
						loginRequest.getPassword()));
				
		/**
		 * Context Setting PART
		 * if the username and password are valid we will have the authentication object here we set the authentication obj
		 * So here we are updating(setting) the SecurityContext to that of authentication.
		 * The Spring Security context will hold the will hold the authentication data for the current request or session
		 */
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		/**
		 * Now we need to generate the token and for token we have generateToken in jwtUtils and for generateToken we need instance of
		 * userDetails beca use we will use username roles in token.
		 * So for that we need the instance of userDetails and this is coming in using the method of getPrincipal method.
		 */
		//Now we get the instance of user details implementation 
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal(); //this will need some typeCasting
		//we need instance of jwtUtils here
		String jwt = jwtUtils.generateToken(userDetails);
		System.out.println("log-> "+ jwt);
		//return the object of jwtAuthenticationResponse to controller
		return new JwtAuthenticationResponse(jwt);
		
	}

	

	/**
	 * @Method : This method helps us to retrive user information based on username 
	 * @param name
	 * @return
	 */
	public User findByUsername(String name) {
 
		// we use userRepository
		return userRepository.findUserByUsername(name).orElseThrow(
			() -> new UsernameNotFoundException("User Not Found with username: "+ name)
		);
	}
}
