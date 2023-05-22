package com.example.springbootrevision.customer;

import com.example.springbootrevision.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }

  public Customer getCustomer(Long customerId) {
    return customerRepository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));
  }
}
