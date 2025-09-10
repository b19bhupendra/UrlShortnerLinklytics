package com.url.shortener.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * This is a class (JwtAuthenticationResposne) that will display the Authentication Response to the user 
 * (This class will represent the  authentication response)
 * 
 * This is a DTO class basically representing the authentication resposne
 * 
 */

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {

	private String token;
	
	public JwtAuthenticationResponse(String token) {
		// TODO Auto-generated constructor stub
		this.token = token;
	}
	
	/**
	 * if JwtAuthenticationResponse has no real getToken(), Spring can’t serialize it.
	 * Spring (via Jackson) can now serialize your JwtAuthenticationResponse into JSON.
	 * @return
	 */
    public String getToken() {
        return token;
    }
}
