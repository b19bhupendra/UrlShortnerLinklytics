package com.url.shortener.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.url.shortener.models.User;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * UserDetails : Its an interface that represent user information that is required for the authentication and authorization within spring security
 * example we have custom user that will be specific to our application and 
 * spring security need to know that which is your user against which we are authenticating the user so,
 * That bridge b/w our custom user model and the spring securities user details implementation need to be created, only then security related features working fine,
 * and for that we creating this particular class here which will do a job of bridging this.
 * So, after this implementation will know that this is the user against which i have to be aware of.
 */
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
	
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String email;
	
	
	private String password;
	
	/*
	 * This GrantedAuthority is how role are managed in the SpringSecurity 
	 */
	private Collection<? extends GrantedAuthority> authorities;

	
	/*
	 * Generating constructors
	 */
	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	/*
	 * implementing build method 
	 * which will help us build the instance of userDetailsImp
	 * THE PURPOSE OF THIS METHOD IS TO convert a user object (from our database) into a userDetailsImpl object for spring security
	 * because spring security is going to make use of this.
	 */
	public static UserDetailsImpl build(User user) {
		//This will extract the role from the user object and convert it into granted authority 
		//because granted security is required by spring security.
		//user.getRole : we are having role in this like role_user, role_admin but this spring security won't understand that so,
		//Spring security need object of granted authority so that it understand what role this particular user has.
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		
		//Then we are returning the instance of on an object with the necessary fields where in we are getting all the information from our user object 
		//and we are creating an object and we are returning it.
		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				Collections.singletonList(authority));
		
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
