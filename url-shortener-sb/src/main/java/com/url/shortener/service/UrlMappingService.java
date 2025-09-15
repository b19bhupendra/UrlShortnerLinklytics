package com.url.shortener.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class UrlMappingService {
	
	private UrlMappingRepository urlMappingRepository;
	/**
	 * @Method : Here will be the actual login of creating the short url
	 * @param originalUrl
	 * @param user
	 * @return
	 */
	public UrlMappingDTO createShortUrl(String originalUrl, User user) {
		
		// generateShortUrl will be a seprate method which will generate a short url.
		// and then we will link this shortUrl to original Url
		String shortUrl =  generateShortUrl(); 
		
		//Instance of urlMapping
		UrlMapping urlMapping = new UrlMapping();
		urlMapping.setOriginalUrl(originalUrl);
		urlMapping.setShortUrl(shortUrl);
		urlMapping.setUser(user);
		urlMapping.setCreatedDate(LocalDateTime.now());
		
		UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
		
		// we will return when returning we will need to send the object of type UrlMappingDTO
		// and urlMAppingDTO have the slightly different structure so we will have seprate method
		return convertToDto(urlMapping);
	}
	
	/**
	 * 
	 * @param urlMapping
	 * @return
	 */
	private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
		
		UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
		
		//setting everything in urlMappinigDto from urlMapping object that we are getting in params
		urlMappingDTO.setId(urlMapping.getId());
		urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
		urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
		urlMappingDTO.setClickCount(urlMapping.getClickCount());
		urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
		urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
		return urlMappingDTO;
	}

	private String generateShortUrl() {

		return "";
	}

}
