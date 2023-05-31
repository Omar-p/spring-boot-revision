package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerPatchRequest;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import com.example.springbootrevision.exception.DuplicateResourceException;
import com.example.springbootrevision.exception.ResourceNotFound;
import com.example.springbootrevision.security.UserDetailsApp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

  private final CustomerDao customerDao;
  private final PasswordEncoder passwordEncoder;

  private static final String CUSTOMER_NOT_FOUND = "Customer with id [%s] does not exist";
  private static final String EMAIL_IS_ALREADY_EXISTS = "Email is already taken";

  public CustomerService(@Qualifier("jpa") CustomerDao customerDao, PasswordEncoder passwordEncoder) {
    this.customerDao = customerDao;
    this.passwordEncoder = passwordEncoder;
  }

  public List<Customer> getCustomers() {
    return customerDao.selectAllCustomers();
  }

  public CustomerDTO getCustomer(Long customerId) {
    return customerDao.selectCustomerById(customerId)
        .map(this::toCustomerDTO)
        .orElseThrow(() -> new ResourceNotFound(CUSTOMER_NOT_FOUND.formatted(customerId)));
  }

  public Long addNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
    if (customerDao.existsByEmail(customerRegistrationRequest.email())) {
      throw new DuplicateResourceException(EMAIL_IS_ALREADY_EXISTS);
    }

    UserDetailsApp userDetailsApp = new UserDetailsApp(
        customerRegistrationRequest.email(),
        passwordEncoder.encode(customerRegistrationRequest.password()),
        true
    );
    final Customer customer = toCustomer(customerRegistrationRequest);
    customer.setUserDetailsApp(userDetailsApp);

    var persistedCustomer = customerDao.insertCustomer(customer);

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
      throw new ResourceNotFound(CUSTOMER_NOT_FOUND.formatted(customerId));
    }
    customerDao.deleteCustomerById(customerId);
  }

  public CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
    var customer = customerDao.selectCustomerById(customerId)
        .orElseThrow(() -> new ResourceNotFound(CUSTOMER_NOT_FOUND.formatted(customerId)));

    if (!customer.getEmail().equals(customerUpdateRequest.email()) && customerDao.existsByEmail(customerUpdateRequest.email())) {
      throw new DuplicateResourceException(EMAIL_IS_ALREADY_EXISTS);
    }

    customer.setName(customerUpdateRequest.name());
    customer.setEmail(customerUpdateRequest.email());
    customer.getUserDetailsApp().setEmail(customerUpdateRequest.email());
    customer.setAge(customerUpdateRequest.age());

    return toCustomerDTO(customerDao.updateCustomer(customer));
  }

  public CustomerDTO updateCustomerPartially(Long customerId, CustomerPatchRequest customerPatchRequest) {
    var customer = customerDao.selectCustomerById(customerId)
        .orElseThrow(() -> new ResourceNotFound(CUSTOMER_NOT_FOUND.formatted(customerId)));

    if (customerPatchRequest.email() != null && customerDao.existsByEmail(customerPatchRequest.email())) {
      throw new DuplicateResourceException(EMAIL_IS_ALREADY_EXISTS);
    }

    customer.setName(Optional.ofNullable(customerPatchRequest.name()).orElse(customer.getName()));
    customer.setEmail(Optional.ofNullable(customerPatchRequest.email()).orElse(customer.getEmail()));
    customer.setAge(Optional.ofNullable(customerPatchRequest.age()).orElse(customer.getAge()));

    return toCustomerDTO(customerDao.updateCustomer(customer));
  }

  public boolean isAuthorized(String email, Long id) {
    var c = customerDao.selectCustomerByEmail(email);
    return c
        .map(customer -> customer.getId().equals(id))
        .orElse(false);
  }

  private CustomerDTO toCustomerDTO(Customer customer) {
    return new CustomerDTO(
        customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getAge(),
        customer.getUserDetailsApp()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList()
    );
  }
}
