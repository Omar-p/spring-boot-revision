package com.example.springbootrevision.security;

import com.example.springbootrevision.customer.CustomerDao;
import com.example.springbootrevision.customer.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  AuthenticationManager customersAuthenticationManager(CustomerUserDetailsService customerUserDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(customerUserDetailsService);
    return new ProviderManager(provider);
  }


  @Bean
  PasswordEncoder  passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
