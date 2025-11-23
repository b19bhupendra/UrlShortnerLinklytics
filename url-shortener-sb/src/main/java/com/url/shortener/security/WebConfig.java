package com.url.shortener.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${frontend.url}") // we need to define this thing in application.properties
	String frontEndUrl;
	
	//Creatiing the bean for mvc configurer
	/**
	 * This code allows your frontend to communicate with your backend by defining CORS permissions.
	 * this is needed because if this code allows your frontend to communicate with your backend by defining CORS permissions.
	 * @return
	 */
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**") // "/**" means this rule applies to all API endpoints.
					.allowedOrigins(frontEndUrl) //Only requests coming from the domain stored in frontendUrl (e.g., http://localhost:3000) are allowed.
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					.allowedHeaders("*")
					.allowCredentials(true)
					.maxAge(3600); //Browser will cache CORS settings for 1 hour (3600 seconds), reducing repeated pre-flight requests.
		}
	}
