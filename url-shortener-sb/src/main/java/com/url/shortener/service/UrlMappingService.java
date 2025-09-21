package com.url.shortener.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.url.shortener.dtos.ClickEventDTO;
import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repository.ClickEventRepository;
import com.url.shortener.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor	
public class UrlMappingService {
	
	@Autowired
	private UrlMappingRepository urlMappingRepository;
	
	@Autowired
	private ClickEventRepository clickEventRepository;
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

	/**
	 * 	
	 * Looking up the URL mapping
	 * Fetching click events from DB
	 * Grouping clicks by day
	 * Counting them
	 * Returning a list of DTOs
	 * ---------------------------------------------------------------------
	 * We will make use of shortUrl to get the urlMapping First
	 * And We will get the whatever clickEvent has been recorded against that particular urlMapping we will get that
	 * Because to get the clickEvent we need to the urlMappingId and urlMappingId can be find in urlMapping Table
	 * @param shortUrl
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
		
		UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
		
		
		if(urlMapping!=null) {
			//if not equal to null then we need to find the urlMapping and the clickDate b/w start and end
			//from the clickEvent Repository
			//This will returnt the List of click events but we expected to return the ClickEventDTO type so we can
			//convert this list into stream 
			return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
					//First convert this into stream and
					//grouping the data by the click date 
					.collect(Collectors.groupingBy(
						    click -> click.getClickDate().toLocalDate(), 
						    Collectors.counting()))
					.entrySet().stream()
						.map(entry -> {
							ClickEventDTO clickEventDTO = new ClickEventDTO();
							// transformation code 
							clickEventDTO.setClickDate(entry.getKey());
							clickEventDTO.setCount(entry.getValue());
							return clickEventDTO;
					})
					.collect(Collectors.toList());
		}
		return null;
	}
	
	/**
	 * 
	 * @param user
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
		//Getting the urlMappings by the users 
		//We need to share the list of urlMApping in the user account that belongs to the user so,
		//Getting all the urlMapping of that particular user
		List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
		
		//List of clickEvents
		//end Date 1 one day = until next day 12 am
		List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusMonths(1).atStartOfDay());                                  
		
		return clickEvents.stream()
				.collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
	}

	/**
	 * @Method : This method return the urlMapping for this shortUrl and 
	 * we are updating the total clickCount at the urlLevel 
	 * and then saving the individual clickCounts object in the clickEvent table
	 *  
	 * @param shortUrl
	 * @return
	 */
	public UrlMapping getOriginalUrl(String shortUrl) {		
		UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
		
		//When the click event happens we have to record that and save it in clickEvent Table.
		//For that we go in if 
		if(urlMapping!=null) {
			//So whatever the current click count is there we will increase by one
			urlMapping.setClickCount(urlMapping.getClickCount() + 1);
			//once clickCount is increased we save
			urlMappingRepository.save(urlMapping);
			
			//Now we have to record the individual clickEvent as well 
			ClickEvent clickEvent = new ClickEvent();
			clickEvent.setClickDate(LocalDateTime.now());
			clickEvent.setUrlMapping(urlMapping);
			clickEventRepository.save(clickEvent);	
		}
		return urlMapping;
	}
}











