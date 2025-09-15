package com.url.shortener.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
	
	private UrlMappingService urlMappingService;
	private UserService userService;
	
	/**
	 * @param Accepts Map of String , String we call this as request 
	 * @param Also we accept the object of type principal , this principal object is auto injected 
	 * 
	 * {"key":"value"} => {"originalUrl" : "https://example.com"}
	 * 
	 * We have not added any authentication related information but we can add annotation of PreAuthorized so 
	 * this will take care of the authentication part.
	 */
	@PostMapping("/shorten")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal){
		
		//First getting the original url that will be passed in the body 
		String originalUrl = request.get("originalUrl");// this is how we get data 
		
		/*
		 * 	We need access to userService because we need to get the user object 
			and we need user object because every url i.e being created is being associated with user in the database 
			and it associated with user because if not then we don't know who created this url 
			and for that reason we are accepting principal over here and principal is being auto injected by spring security
			because this is an authenticated request that we are adding and springsecurity will be aware of this 
			who the user is based on what we set in security context.
		 */
		User user = userService.findByUsername(principal.getName());
		
		//Now we will create short url so calling service method and this should return UrlMappingDTO object
		UrlMappingDTO  urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
		
		return ResponseEntity.ok(urlMappingDTO);
		
	}
	

}











