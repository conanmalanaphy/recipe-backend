package com.mycookbookapp.recipe_backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to all /api endpoints
                .allowedOrigins(
                        "http://localhost:4321", // Your Astro dev server
                        "https://recipe-backend-3ata.onrender.com" // Your deployed Astro frontend (once you deploy it)
                        // Add more origins here if your frontend is deployed elsewhere later
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow cookies and authentication headers
                .maxAge(3600); // Max age of preflight request cache
    }
}