package com.mycookbookapp.recipe_backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4321", // For your local Astro dev server
                        "https://recipe-backend-3ata.onrender.com",
                        "https://recipe-frontend-jvcn.onrender.com" // <--- ADD THIS EXACT URL for your deployed frontend!
                        // Ensure there are no typos, and it's HTTPS
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}