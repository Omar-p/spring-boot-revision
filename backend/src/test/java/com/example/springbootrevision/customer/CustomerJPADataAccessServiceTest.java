package com.example.springbootrevision.customer;

import com.example.springbootrevision.AbstractPGTestcontainer;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class CustomerJPADataAccessServiceTest {

  private CustomerJPADataAccessService underTest;

//  private AutoCloseable closeable;

  @Mock
  private CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
//    closeable = MockitoAnnotations.openMocks(this);
    underTest = new CustomerJPADataAccessService(customerRepository);
  }
  @Test
  void getCustomersTest() {

    underTest.selectAllCustomers();

    BDDMockito.verify(customerRepository)
        .findAll();
  }


  @Test
  void getCustomerByIdTest() {
    Long id = 1L;

    underTest.selectCustomerById(id);

    BDDMockito.verify(customerRepository)
        .findById(id);
  }


  @Test
  void itShouldInsertCustomer() {
    // Given
    Customer customer = new Customer(
        "John",
        "email@email.com",
        20
    );
    // When
    underTest.insertCustomer(customer);

    // Then
    BDDMockito.verify(customerRepository)
        .save(customer);
  }

  @Test
  void givenEmailItShouldCallExistsByEmail() {
    // Given
    String email = "email@email.com";
    // When
    underTest.existsByEmail(email);

    // Then
    BDDMockito.verify(customerRepository)
        .existsByEmail(email);

  }

  @Test
  void givenIdItShouldCallExistsById() {
    // Given
    Long id = 1L;
    // When
    underTest.existsById(id);

    // Then
    BDDMockito.verify(customerRepository)
        .existsById(id);
  }

  @Test
  void givenCustomerIdItShouldCallDeleteCustomerById() {
    // Given
    Long id = 1L;
    // When
    underTest.deleteCustomerById(id);

    // Then
    BDDMockito.verify(customerRepository)
        .deleteById(id);
  }

  @Test
  void itShouldUpdateCustomer() {
    // Given
    Customer customer = new Customer(
        1L,
        "John",
        "email@email.com",
        15
    );
    // When
    underTest.updateCustomer(customer);
    // Then
    BDDMockito.verify(customerRepository)
        .save(customer);
  }
}