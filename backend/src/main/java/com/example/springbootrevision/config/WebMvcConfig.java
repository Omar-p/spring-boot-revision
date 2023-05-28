package com.example.springbootrevision.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("#{'${cors.allowed-origins}'.split(',')}")
  private List<String> allowedOrigins;

  @Value("#{'${cors.allowed-methods}'.split(',')}")
  private List<String> allowedMethods;

  @Override
  public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigins.toArray(new String[0]))
        .allowedMethods(allowedMethods.toArray(new String[0]));
  }
}
