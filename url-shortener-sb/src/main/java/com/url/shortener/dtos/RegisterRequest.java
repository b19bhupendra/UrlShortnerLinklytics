package com.url.shortener.dtos;

import java.util.Set;

import lombok.Data;

/*
 * DTO : Data Transfer Object - which is defining a structure of a request for registration 
 * 
 * Here user can send userName, email, role, password in the registrationRequest
 */
@Data
public class RegisterRequest {
	
	private String userName;
	
	public String getUsername() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	private String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private Set<String> role;
	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	private String password;
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
