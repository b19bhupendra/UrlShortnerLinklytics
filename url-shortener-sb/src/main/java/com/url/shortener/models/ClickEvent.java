package com.url.shortener.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ClickEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime clickDate;
	
	
	/*
	 * This is an many to one relationship with urlMapping class and clickEvent is the many side of the relationship
	 * Join Column indicates url_mapping_id is a foreign key in the database that links the click event to the url mapping 
	 */
	@ManyToOne
	@JoinColumn(name = "url_mapping_id")
	private UrlMapping urlMapping;

}
