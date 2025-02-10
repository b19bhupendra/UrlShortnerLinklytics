package com.url.shortener.security.jwt;

import lombok.Data;

/*
 * This is a class (JwtAuthenticationResposne) that will display the Authentication Response to the user
 * This is a DTO class basically representing the authentication resposne
 */

@Data
public class JwtAuthenticationResponse {
	
	private String token;

}
