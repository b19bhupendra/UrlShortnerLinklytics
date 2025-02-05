package com.url.shortener.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class UrlMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int clickCount = 0;
	
	private String originalUrl;
	
	private String shortUrl;
		
	private LocalDateTime createdDate;
	
	
	/*
	 * Here we are creating many to one relationship with user 
	 * Many url mappings can belong to single user
	 * JoinCloumn  specifies foregin key linking of this table to the user table  
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	/*
	 * We are defining one to many relationship with click event class 
	 * mappedBy indicates that URL mapping field in the click event class owns the relationship
	 * And we are storing the list of click event that are happens here in the form of list so these are all 
	 * objects of type click event that is being stored in the form of list
	 */
	@OneToMany(mappedBy = "urlMapping")
	private List<ClickEvent> clickEvents;

}
