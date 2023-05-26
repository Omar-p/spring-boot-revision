package com.example.springbootrevision.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

  List<Customer> selectAllCustomers();
  Optional<Customer> selectCustomerById(Long id);
  Customer insertCustomer(Customer customer);

  boolean existsByEmail(String email);

  boolean existsById(Long id);
  void deleteCustomerById(Long id);
  Customer updateCustomer(Customer customer);

}
