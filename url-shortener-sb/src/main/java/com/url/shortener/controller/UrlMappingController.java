package com.url.shortener.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortener.dtos.ClickEventDTO;
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
	
    private static final Logger logger = LoggerFactory.getLogger(UrlMappingController.class);

	
    // Constructor injection (Spring will auto-wire beans)
    public UrlMappingController(UrlMappingService urlMappingService, UserService userService) {
        this.urlMappingService = urlMappingService;
        this.userService = userService;
    }
    
	/**
	 * @param Accepts Map of String , String we call this as request 
	 * @param Also we accept the object of type principal , this principal object is auto injected 
	 * 
	 * {"key":"value"} => {"originalUrl" : "https://example.com"}
	 * 
	 * https://base.com/Qf65YTpi ---redirect---> https://example.com  (TODO :  redirection logic)
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
		 * Getting the USER
		 * 
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
	
	/**
	 * EndPoint that will helpUs to get all the urls per particular user 
	 * Principal is a Java interface that represents the currently authenticated user.
	 * principal.getName() → gives you the username of the logged-in user.
	 * 
	 * And this api will be used in frontedn to display url that user has shortend based on logged in use 
	 */
	@GetMapping("/myUrls")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
		
		//First Getting the user 
		User user = userService.findByUsername(principal.getName());
		
		//Then getting urls for a particular user using this service class
		List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
		
		return ResponseEntity.ok(urls);
	}
	
	

	/**
	 * @Method This request helps us to get the analytics for a particular shortUrl within a particular date range.
	 * @param shortUrl
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/analytics/{shortUrl}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, 
															@RequestParam("startDate") String startDate,
															@RequestParam("endDate") String endDate){
														
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;// example : 2024-12-01T00:00:00
		
		//Converting startDate and lastDate in our localDate and Time object
		LocalDateTime start = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);
		//This will give list of click events dto
		List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start, end);
		
		return ResponseEntity.ok(clickEventDTOS);
	}
	
	/**
	 * We need user here because here we need total clicks for all the urls that user owns so here we need to know who the user is.
	 * that is why we want principal here.
	 * @param principal
	 * @return
	 */
	@GetMapping("/totalClicks")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
												@RequestParam("startDate") String startDate,
												@RequestParam("endDate") String endDate){
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;// example : 2024-12-01T00:00:00
		
		User user = userService.findByUsername(principal.getName());
		//Converting startDate and lastDate in our localDate and Time object
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		//This will give Map of total clicks
		Map <LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);
		
		return ResponseEntity.ok(totalClicks);
	
	}

}





























