package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerPatchRequest;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import com.example.springbootrevision.jwt.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CustomerController {


  private final CustomerService customerService;
  private final JWTUtil jwtUtil;

  @GetMapping("api/v1/customers")
  public List<Customer> getCustomers() {
    return customerService.getCustomers();
  }

  @GetMapping("api/v1/customers/{customerId}")
  public CustomerDTO getCustomer(@PathVariable("customerId") Long customerId) {
    return customerService.getCustomer(customerId);
  }


  @PostMapping("api/v1/customers")
  public ResponseEntity<?> registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
    final Long id = customerService.addNewCustomer(customerRegistrationRequest);
    return ResponseEntity
        .created(URI.create("/api/customers/" +id))
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + issueTokeWithRoleUser(customerRegistrationRequest))
        .build();
  }

  private String issueTokeWithRoleUser(CustomerRegistrationRequest customerRegistrationRequest) {
    return jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
  }

//  @DeleteMapping("api/v1/customers/{customerId}")
//  @ResponseStatus(HttpStatus.NO_CONTENT)
//  public void deleteCustomer(@PathVariable("customerId") Long customerId) {
//    customerService.deleteCustomer(customerId);
//  }

  @PutMapping("api/v1/customers/{customerId}")
  @PostAuthorize("@customerService.isAuthorized(authentication.name, #customerId)")
  public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("customerId") Long customerId,
                                                    @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
    var updatedCustomer = customerService.updateCustomer(customerId, customerUpdateRequest);
    return ResponseEntity.ok(updatedCustomer);
  }


  @PatchMapping("api/v1/customers/{customerId}")
  @PreAuthorize("@customerService.isAuthorized(authentication.name, #customerId)")
  public ResponseEntity<CustomerDTO> updateCustomerPartially(@PathVariable("customerId") Long customerId, @Valid @RequestBody CustomerPatchRequest customerPatchRequest) {
    var updatedCustomer = customerService.updateCustomerPartially(customerId, customerPatchRequest);
    return ResponseEntity.ok(updatedCustomer);
  }




}
