package com.url.shortener.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Value;

import com.url.shortener.service.UserDetailsImpl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

/*
 * This is the file that will have all the utility helper method that our application going to need
 */
public class JwtUtils {

	/*
	 * These two fields will be injected from application.properties
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.expiration}")
	private int jwtExpirationsMs;
	
	/*
	 * Authorization -> Bearer <TOKEN>
	 * Method : Extracting JWTtoken from the header to validate it.
	 * SO this method is a helper method that is going to help us to extract the JWTtoken from the header.
	 */
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization"); 
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);//removing Bearer from the token and returning actual token
		}
		return null;
	}
	
	/*
	 * Method : TO generate the token 
	 * params : we are acceptng object of userDetailsImpl type 
	 * because we want to have roles and username embeddedinto the token when generating it.
	 * 
	 */
	public String generateToken(UserDetailsImpl userDetails) {
		//Getting the User-name 
		String username = userDetails.getUsername();
		//converting it into stream because we want the list
		String roles = userDetails.getAuthorities().stream()
				.map(authority -> authority.getAuthority())
				.collect(Collectors.joining(",")); 
		return Jwts.builder()
				.setSubject(username)
				.claim("roles",roles)
				.issuedAt(new Date())
				.expiration(new Date((new Date().getTime() + jwtExpirationsMs)))
				.signWith(key())
				.compact();
	}
	
	/*
	 * There will be scenario where we want to extract name JWTtoken that is what this method will help us.
	 */
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key()) // We are verifying it first with the key
				.build().parseSignedClaims(token) //then with the help of build we are parsing the token  
				.getPayload().getSubject();// then getting the subject
	}
	
	/*
	 * Private method for getting the key
	 * This key is an instance of java.security
	 */
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	/*
	 * Validating token 
	 */
	public boolean validateToken (String authToken) {
		try {
			Jwts.parser().verifyWith((SecretKey) key()) // if this line succeds then token is valid 
			.build().parseSignedClaims(authToken);
			return true;
		} catch (JwtException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

	}
	
}







