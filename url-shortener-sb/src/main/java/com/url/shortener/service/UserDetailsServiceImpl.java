package com.url.shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.url.shortener.models.User;
import com.url.shortener.repository.UserRepository;

import jakarta.transaction.Transactional;

/*
 * In this class we will define our custom implementation (logic)
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	/*
	 * This repository will help to interact with user model
	 */
	@Autowired
	private UserRepository userRepository;
	
	/*
	 * This method will return userDetails
	 * (which userDetails) : This userDetails is of springSecurity 
	 * 
	 * It will load the data inside the filter (JwtAuthenticationFilter) because we are calling this method there 
	 * and this user data will accepted there and verified, validated and authentication context will be set
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//This user is our database user
		//the bellow line will load the user from our database
		User user = userRepository.findUserByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("Username Not Found with username: "+ username));
		//and it is going to convert user into the object type that sprig security understand 
		
		return UserDetailsImpl.build(user);
	}

}
