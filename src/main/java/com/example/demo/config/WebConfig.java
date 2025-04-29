package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Cho phép tất cả các endpoint
                .allowedOrigins("http://localhost:5173")  // Chỉ cho phép yêu cầu từ localhost:5173
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")  // Cho phép các phương thức HTTP này
                .allowedHeaders("*")  // Cho phép tất cả các headers
                .allowCredentials(true);  // Cho phép gửi cookies
    }
}
