package com.example.springbootrevision.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>  {

  Optional<Customer> findCustomerByEmail(String email);

  boolean existsByEmail(String email);


  Optional<Customer> findByEmail(String email);
}
