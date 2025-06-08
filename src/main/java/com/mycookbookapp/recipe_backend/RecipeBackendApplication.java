package com.mycookbookapp.recipe_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Import Bean
import org.springframework.web.client.RestTemplate; // Import RestTemplate

@SpringBootApplication
public class RecipeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeBackendApplication.class, args);
	}

	// Add this bean to make RestTemplate available for injection
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
