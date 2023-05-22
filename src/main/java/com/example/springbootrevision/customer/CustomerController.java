package com.example.springbootrevision.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CustomerController {


  private final CustomerService customerService;

  @GetMapping("api/v1/customers")
  public List<Customer> getCustomers() {
    return customerService.getCustomers();
  }

  @GetMapping("api/v1/customers/{customerId}")
  public Customer getCustomer(Long customerId) {
    return customerService.getCustomer(customerId);
  }


}
