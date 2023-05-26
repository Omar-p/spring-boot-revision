package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerPatchRequest;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
  public Customer getCustomer(@PathVariable("customerId") Long customerId) {
    return customerService.getCustomer(customerId);
  }


  @PostMapping("api/v1/customers")
  public ResponseEntity<?> registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
    final Long id = customerService.addNewCustomer(customerRegistrationRequest);
    return ResponseEntity.created(URI.create("/api/customers/" +id)).build();
  }

  @DeleteMapping("api/v1/customers/{customerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCustomer(@PathVariable("customerId") Long customerId) {
    customerService.deleteCustomer(customerId);
  }

  @PutMapping("api/v1/customers/{customerId}")
  public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") Long customerId, @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
    final Customer updatedCustomer = customerService.updateCustomer(customerId, customerUpdateRequest);
    return ResponseEntity.ok(updatedCustomer);
  }


  @PatchMapping("api/v1/customers/{customerId}")
  public ResponseEntity<Customer> updateCustomerPartially(@PathVariable("customerId") Long customerId, @Valid @RequestBody CustomerPatchRequest customerPatchRequest) {
    final Customer updatedCustomer = customerService.updateCustomerPartially(customerId, customerPatchRequest);
    return ResponseEntity.ok(updatedCustomer);
  }




}
