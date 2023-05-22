package com.example.springbootrevision.customer;

import com.example.springbootrevision.exception.DuplicateResourceException;
import com.example.springbootrevision.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  public Long addNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
    if (customerRepository.existsByEmail(customerRegistrationRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    var persistedCustomer = customerRepository.save(toCustomer(customerRegistrationRequest));

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
    if (!customerRepository.existsById(customerId)) {
      throw new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId));
    }
    customerRepository.deleteById(customerId);
  }

  public Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
    var customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));

    if (customerRepository.existsByEmail(customerUpdateRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    customer.setName(customerUpdateRequest.name());
    customer.setEmail(customerUpdateRequest.email());
    customer.setAge(customerUpdateRequest.age());

    return customerRepository.save(customer);
  }

  public Customer updateCustomerPartially(Long customerId, CustomerPatchRequest customerPatchRequest) {
    var customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] does not exist".formatted(customerId)));

    if (customerPatchRequest.email() != null && customerRepository.existsByEmail(customerPatchRequest.email())) {
      throw new DuplicateResourceException("Email is already taken");
    }

    customer.setName(Optional.ofNullable(customerPatchRequest.name()).orElse(customer.getName()));
    customer.setEmail(Optional.ofNullable(customerPatchRequest.email()).orElse(customer.getEmail()));
    customer.setAge(Optional.ofNullable(customerPatchRequest.age()).orElse(customer.getAge()));

    return customerRepository.save(customer);
  }
}
