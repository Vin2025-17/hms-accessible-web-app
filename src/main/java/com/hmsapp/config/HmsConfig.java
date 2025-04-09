package com.hmsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HmsConfig implements WebMvcConfigurer {

    private JwtFilter jwtFilter;

    public HmsConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Security configuration to enable CORS and JWT filter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Ensure the jwtFilter is applied before the UsernamePasswordAuthenticationFilter (default Spring filter)
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Enable CORS and disable CSRF for API endpoints
        http.cors().and().csrf().disable();

        // Define authorization settings (allow all for now, you can restrict later)
        http.authorizeRequests().anyRequest().permitAll();

        return http.build();
    }

    // CORS configuration
  //  @Override
   // public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from your React app running on localhost:3000
       // registry.addMapping("/**")
//.allowedOrigins("http://localhost:3000") // Allow specific origins
               // .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
               // .allowedHeaders("*") // Allow all headers
               // .allowCredentials(true); // Allow cookies and credentials if needed
    }

