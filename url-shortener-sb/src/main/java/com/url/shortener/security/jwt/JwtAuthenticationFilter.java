package com.url.shortener.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This is a filter that will make sure that every request has JWT token
 * OncePerRequestFilter is an abstract class It aims to guaranty a single execution per request
 * So for every request this class code will be executed.
 * And we have not defined this class so we can create an implementation for this in which we can override loadUserByUsername to define 
 * our own implementation.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtTokenProvider;
	
	/*
	 * This is an interface and it is defined by spring security 
	 * and it helps the logic for loading the user specific data so using UserDetailsService we can load the user specific data
	 */
	@Autowired
	private UserDetailsService userDetailsService;
	

	/*
	 * 1. Extracting the JWT token from the header : because using jwt we will know wether the user is a valid user or not.
	 * 2. Validating the token
	 * 3.If Valid get user details : We can get the details from the token itself.
	 *   (when we have created the token we have set the user details in that in jwtUtils class we have help method(getUsernameFromJwtToken) as well in that class.
	 * 4.-- Get user name -> load User -> Set the authentication context.
	*/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			//1
			String jwt = jwtTokenProvider.getJwtFromHeader(request);
			//2:If the token is not null and also valid then we can getting the user , loading the user and setting the authentication context 
			if(jwt != null && jwtTokenProvider.validateToken(jwt)) {
				//3
				String username = jwtTokenProvider.getUserNameFromJwtToken(jwt);
				//4 and (userdata will be accepted here from the UserDetailsService here as
				// we are calling method loadUserByUsername of UserDetailsService as this class is implemented by userDetailsServiceImpl)
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if(userDetails != null) {
					//Creating the object of userName and password Authentication Token (initializing springSecurity context)
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					//updating the security context (updating authentication related information)
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * The bellow line will help filter chain to continue as we have added our filter in between the filter-chain
		 * so there are filters before this and after this so we have to add this to continue filter chain
		 * not adding bellow line will break the chain
		 */
		filterChain.doFilter(request, response);
	}

	
}
