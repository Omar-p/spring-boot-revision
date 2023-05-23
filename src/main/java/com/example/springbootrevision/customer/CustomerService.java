package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerPatchRequest;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import com.example.springbootrevision.exception.DuplicateResourceException;
import com.example.springbootrevision.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

  private final CustomerDao customerDao;

  public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public List<Customer> getCustomers() {
    return customerDao.selectAllCustomers();
  }

  public Customer getCustomer(Long customerId) {
    return customerDao.selectCustomerById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));
  }

  public Long addNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
    if (customerDao.existsByEmail(customerRegistrationRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    var persistedCustomer = customerDao.insertCustomer(toCustomer(customerRegistrationRequest));

    return persistedCustomer.getId();
  }

  private Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
    return new Customer(
        customerRegistrationRequest.name(),
        customerRegistrationRequest.email(),
        customerRegistrationRequest.age()
    );
  }

  public void deleteCustomer(Long customerId) {
    if (!customerDao.existsById(customerId)) {
      throw new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId));
    }
    customerDao.deleteCustomerById(customerId);
  }

  public Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
    var customer = customerDao.selectCustomerById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));

    if (customerDao.existsByEmail(customerUpdateRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    customer.setName(customerUpdateRequest.name());
    customer.setEmail(customerUpdateRequest.email());
    customer.setAge(customerUpdateRequest.age());

    return customerDao.insertCustomer(customer);
  }

  public Customer updateCustomerPartially(Long customerId, CustomerPatchRequest customerPatchRequest) {
    var customer = customerDao.selectCustomerById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));

    if (customerPatchRequest.email() != null && customerDao.existsByEmail(customerPatchRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    customer.setName(Optional.ofNullable(customerPatchRequest.name()).orElse(customer.getName()));
    customer.setEmail(Optional.ofNullable(customerPatchRequest.email()).orElse(customer.getEmail()));
    customer.setAge(Optional.ofNullable(customerPatchRequest.age()).orElse(customer.getAge()));

    return customerDao.updateCustomer(customer);
  }
}
