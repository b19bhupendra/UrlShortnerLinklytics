package com.url.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortener.models.UrlMapping;
import com.url.shortener.service.UrlMappingService;

@RestController
public class RedirectController {
	
	//Object of UrlMapping
	@Autowired
	private UrlMappingService urlMappingService;
	
	/**
	 * 
	 * @param shortUrl
	 * @return
	 */
	@GetMapping("/{shortUrl}")
	public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
		
		//This getOriginalUrl will return the urlMapping for this short
		UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
		
		if(urlMapping!=null) {
			//We will use Http headers for redirecting 
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location",urlMapping.getOriginalUrl());
			return ResponseEntity.status(302).headers(httpHeaders).build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}

}
