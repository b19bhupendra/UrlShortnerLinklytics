package com.url.shortener.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long>{
	
	//List of click event
	//So findByUrlMapping and ClickDateBetween so there are two dates that are passed so JPA will automatically 
	//convert it into Between clause
	//Here JPA will filter all the data by this UrlMapping and also between these two dates that is how the filter is being work
	List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping mapping, LocalDateTime startDate, LocalDateTime endDate);


	//This is another method 
	//This is going to return the list of UrlMapping
	List<ClickEvent> findByUrlMappingInAndClickDateBetween(List <UrlMapping> mapping, LocalDateTime startDate, LocalDateTime endDate);


}
