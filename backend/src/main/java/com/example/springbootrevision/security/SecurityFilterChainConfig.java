package com.example.springbootrevision.security;

import com.example.springbootrevision.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainConfig {

  private final AuthenticationManager authenticationManager;

  private final JWTAuthenticationFilter jwtAuthenticationFilter;

  public SecurityFilterChainConfig(AuthenticationManager authenticationManager, JWTAuthenticationFilter jwtAuthenticationFilter) {
    this.authenticationManager = authenticationManager;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST,  "/api/v1/customers").permitAll()
            .anyRequest().authenticated()
        )
        .authenticationManager(authenticationManager)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }


}
