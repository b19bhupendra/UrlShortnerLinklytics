package com.url.shortener.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
/**
 * 
 * @author Bhupendra
 *  
 * In this we have sevral methods so that we can get the urlMapping object using shortUrl
 * and we also need to get all the urlMappings of the   user
 *
 */
@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
	
	//There will be scnerios where we searching a record by shortUrl
	UrlMapping findByShortUrl(String shortUrl);
	 
	//All the url of particular user so we are able to get the urlmappings by user
	List<UrlMapping> findByUser(User user);
	

}
