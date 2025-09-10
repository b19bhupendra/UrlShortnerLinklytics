package com.url.shortener.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.url.shortener.security.jwt.JwtAuthenticationFilter;
import com.url.shortener.service.UserDetailsServiceImpl;

/*
 * Custom security configuration for our application
 * @configuration because its our configuration class
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	/*
	 * @Bean is an annotation used to indicate that a method should return an object 
	 * @Returns : object
	 */
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Bean for authenticationManager
	 * @return
	 * @throws Exception 
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	/*
	 * b.Configure DaoAuthenticationProvider:
	 * 1.Sets up how authentication is handled by Spring security
	 * 2.Bean for AuthenticationManager and PasswordEncoder
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		//Instance of DaoAuthenticationProvider
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		//userDetailsService which defines how information is fetched
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
		
	}
	
	
	
	
	/*
	 * a.Custom security configuration
	 * 
	 * Configure JwtAuthenticationFilter in FilterChain : 
	 * 1. Spring Security recognizes it as a filter chain
	 * 2.By default, SpringSecurity does't automatically include your custom filter (JwtAuthenticationFilter) in the filterChain
	 * unless we explicitly add it.
	 * 
	 * short url = http://domain.com/xyz --> google.com so http://domain.com/xyz should be accessible publicly so if its a short url
	 * then permit all 
	 * 
	 * @Return : Object of type security filter chain
	 */
	
	@Bean // if we miss this annotation then entire method won't be picked up it will be ignored
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(AbstractHttpConfigurer::disable) //CSRF : Protection
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers("/api/urls/**").authenticated()
					.requestMatchers("/{shortUrl}").permitAll()
					.anyRequest().authenticated()
					);
		
		/*
		 * We are here telling  springSecurity that before this (UsernamePasswordAuthenticationFilter) in the filterChain 
		 * add this (jwtAuthenticationFilter) custom filter. 
		 * 
		 * Also setting authenticationProvider
		 */
		http.authenticationProvider(authenticationProvider());
		//Spring security knows jwtAuthenticationFilter need to be excuted only once but it does not know where in security filter 
		//chain so addFilterBefore 
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build(); // return the object of type security filter chain
	}

}
