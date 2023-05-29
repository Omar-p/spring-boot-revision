package com.example.springbootrevision.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

  private final CustomerRepository customerRepository;

  @Override
  public List<Customer> selectAllCustomers() {
    return customerRepository.findAll();
  }

  @Override
  public Optional<Customer> selectCustomerById(Long id) {
    return customerRepository.findById(id);
  }

  @Override
  public Customer insertCustomer(Customer customer) {

    return customerRepository.save(customer);
  }

  @Override
  public boolean existsByEmail(String email) {
    return customerRepository.existsByEmail(email);
  }

  @Override
  public boolean existsById(Long id) {
    return customerRepository.existsById(id);
  }

  @Override
  public void deleteCustomerById(Long id) {
    customerRepository.deleteById(id);
  }

  @Override
  public Customer updateCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  @Override
  public Optional<Customer> selectCustomerByEmail(String email) {

    return customerRepository.findByEmail(email);
  }
}
