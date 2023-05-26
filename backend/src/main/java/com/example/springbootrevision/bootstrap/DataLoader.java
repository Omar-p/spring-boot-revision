package com.example.springbootrevision.bootstrap;

import com.example.springbootrevision.customer.Customer;
import com.example.springbootrevision.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

  @Bean
  CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
    return args -> {
      var faker = new Faker();
      Random random = new Random();
      for (int i = 0; i < 250; i++) {
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        int age = random.nextInt(16, 99);
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + random.nextInt() + "@gmail.com";
        Customer customer = new Customer(
            firstName + " " + lastName,
            email,
            age
        );
        customerRepository.save(customer);
      }

    };
  }
}
