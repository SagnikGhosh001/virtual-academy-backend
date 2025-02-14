package com.smsv2.smsv2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*") // Allow all origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Include OPTIONS for preflight
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true); // Allow cookies and credentials
    }
}
