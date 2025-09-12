package com.url.shortener.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class UrlMappingService {
	
	@Autowired
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
		return convertToDto(savedUrlMapping);
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

	/**
	 * Logic for generating short url : First we will have lenght of the short url set to 8 
	 * and then we will generate any random character and we will apend it until the lenght is 8 that is the simple logic that we will apply 
	 * 
	 * @return
	 */
	private String generateShortUrl() {
		
		//List of characters from where the random letters are being generated;
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWQYZabcdefghijklmnopqrstuvwxyz0123456789";
		
		Random random = new Random();
		
		//using StringBuilder
		StringBuilder shortUrl = new StringBuilder(8);
		
		for(int i = 0; i<8; i++) {
			shortUrl.append(characters.charAt(random.nextInt(characters.length())));
		}
		
		//Converting shortUrl of type stringbuilder to String using toString
		return shortUrl.toString();
	}

	/**
	 * 
	 * @param user
	 * @return UrlMappingDTO
	 */
	public List <UrlMappingDTO> getUrlsByUser(User user) {

		/* this returns the list of url mapping but we need a list of urlMappingDTO 
			so for every mapping in the list we are maping every item to DTO (converting) 
			then collecting as list  and returning
		*/
		return urlMappingRepository.findByUser(user).stream()
				.map(this::convertToDto) //Converting all the object to DTO so calling convertToDto method
				.toList();// Then collecting as a list\
	}

}
