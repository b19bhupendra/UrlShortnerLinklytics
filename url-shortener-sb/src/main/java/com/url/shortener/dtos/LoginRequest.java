package com.url.shortener.dtos;

import lombok.Data;

@Data
public class LoginRequest {
	
	
	private String userName;
	public String getUsername() {
		return userName;
	}
	public void setUsername(String userName) {
		this.userName =  userName;
	}
	
	private String password;
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
