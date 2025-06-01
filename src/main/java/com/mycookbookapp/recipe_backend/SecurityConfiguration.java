package com.mycookbookapp.recipe_backend; // <-- CORRECT THIS LINE!

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer; // For HeadersConfigurer.FrameOptionsConfig::disable


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	@Order(1)
	public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher(new AntPathRequestMatcher("/h2-console/**"))
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().permitAll()
				)
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
				)
				.headers(headers -> headers
						.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // Use disable()
				);
		return http.build();
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				// Temporarily permit all requests to API endpoints for testing
				// We'll re-add proper authentication later.
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll() // <--- NEW: Permit all /api requests
						.anyRequest().authenticated() // All other requests still require authentication
				)
				.formLogin(Customizer.withDefaults()); // This form login applies only to non-api routes now

		// You also need to disable CSRF for API POST/PUT/DELETE requests when using tools like curl/Postman
		// as they don't send CSRF tokens by default. For a real app, you'd use token-based auth like JWT.
		http.csrf(csrf -> csrf.disable()); // <--- IMPORTANT: Disable CSRF globally for now (we'll make it smarter later)

		return http.build();
	}
}