package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import com.example.springbootrevision.exception.ResourceNotFound;
import com.example.springbootrevision.security.UserDetailsApp;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class CustomerServiceTest {

  @Mock
  CustomerDao customerDao;

  @Mock
  PasswordEncoder passwordEncoder;

  @InjectMocks
  private CustomerService underTest;

  @Test
  void itShouldGetCustomers() {

    underTest.getCustomers();

    BDDMockito.verify(customerDao)
        .selectAllCustomers();
  }

  @Test
  void givenExisitingIdItShouldGetCustomer() {
    // Given
    final Customer customer = new Customer(1L, "James", "Bond@email.com", 22);
    customer.setUserDetailsApp(new UserDetailsApp(
        "James",
        "password",
        true
    ));
    BDDMockito.given(customerDao.selectCustomerById(1L))
        .willReturn(Optional.of(customer));


    // When

    underTest
        .getCustomer(1L);

    // Then
    BDDMockito.verify(customerDao)
        .selectCustomerById(1L);
  }

  @Test
  void givenNonExisitingIdItShouldGetCustomer() {
    // Given
    BDDMockito.given(customerDao.selectCustomerById(1L))
        .willReturn(Optional.empty());



    BDDAssertions.assertThatThrownBy(() -> underTest.getCustomer(1L))
        .isInstanceOf(ResourceNotFound.class)
        .hasMessageContaining("Customer with id [%s] does not exist".formatted(1));

    BDDMockito.verify(customerDao)
        .selectCustomerById(1L);
  }



  @Test
  void itShouldAddNewCustomer() {
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(
        "James",
        "Bond@email.com",
        22,
        "password"
    );

    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

    BDDMockito.given(customerDao.existsByEmail(emailArgumentCaptor.capture()))
        .willReturn(false);


    final Customer retCustomer = new Customer(
        1L,
        request.name(),
        request.email(),
        request.age()
    );
    BDDMockito.given(customerDao.insertCustomer(customerArgumentCaptor.capture()))
        .willReturn(retCustomer);

    var c = underTest.addNewCustomer(request);

    BDDAssertions.assertThat(emailArgumentCaptor.getValue())
        .isEqualTo(request.email());

    BDDAssertions.assertThat(customerArgumentCaptor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .ignoringFields("userDetailsApp")
        .isEqualTo(retCustomer);

    BDDAssertions.assertThat(c)
        .isEqualTo(1L);
  }

  @Test
  void givenNotExistingWhenDeleteCustomerItShouldThrowResourceNotFound() {
    // Given
    BDDMockito.given(customerDao.existsById(1L))
        .willReturn(false);
    // When

    BDDAssertions.assertThatThrownBy(() -> underTest.deleteCustomer(1L))
        .isInstanceOf(ResourceNotFound.class)
        .hasMessageContaining("Customer with id [%s] does not exist".formatted(1));


  }

  @Test
  void givenExistingWhenDeleteCustomerItShouldDelete() {
    // Given
    BDDMockito.given(customerDao.existsById(1L))
        .willReturn(true);
    // When

    underTest.deleteCustomer(1L);

    // Then
    BDDMockito.verify(customerDao)
        .deleteCustomerById(1L);

  }

  @Test
  void givenValidCustomerUpdateRequestItShouldUpdate() {
    // Given
    Long id = 1L;
    var cur = new CustomerUpdateRequest(
        "James",
        "newbond@email.com",
        22
    );

    final Customer customer = new Customer(
        id,
        "Jamess",
        "bond@email.com",
        22
    );
    customer.setUserDetailsApp(new UserDetailsApp(
        "newbond@email.com",
        "password",
        true
    ));

    BDDMockito.given(customerDao.selectCustomerById(id))
        .willReturn(Optional.of(customer));

    BDDMockito.given(customerDao.updateCustomer(customer))
        .willReturn(customer);

    BDDMockito.given(customerDao.existsByEmail(cur.email()))
        .willReturn(false);


    // When
    underTest.updateCustomer(id, cur);

    // Then
    ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
    BDDMockito.verify(customerDao)
        .updateCustomer(customerArgumentCaptor.capture());

    BDDAssertions.assertThat(customerArgumentCaptor.getValue().getEmail())
        .isEqualTo(cur.email());
  }

  @Test
  @Disabled("TODO: implement")
  void itShouldUpdateCustomerPartially() {
    // Given
    // When

    // Then
  }
}