package com.example.springbootrevision.customer;

import com.example.springbootrevision.AbstractPGTestcontainer;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerJdbcDataAccessServiceTest extends AbstractPGTestcontainer {

  private final CustomerJdbcDataAccessService underTest;

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public CustomerJdbcDataAccessServiceTest(JdbcTemplate jdbcTemplate) {
    this.underTest = new CustomerJdbcDataAccessService(jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  @BeforeEach
  void setUp() {
    jdbcTemplate.update(
        "INSERT INTO user_details_app(email, password, enabled) VALUES ('email@email.com', 'password', true)"
    );

    jdbcTemplate.update(
    """
    INSERT INTO customer(name, email, age, user_details_app_id)
    VALUES ('John', 'email@email.com', 20, currval('user_details_app_id_sequence'))
    """);

  }

  @Test
  void itShouldSelectAllCustomers() {

    final List<Customer> customers = underTest.selectAllCustomers();

    BDDAssertions.assertThat(customers)
        .hasSize(1)
        .extracting("name")
        .containsExactly("John");
  }

  @Test
  void itShouldSelectCustomerById() {
    Long id = jdbcTemplate.queryForObject(
        """
            SELECT id
            FROM customer
            WHERE name = 'John'
            """,
        Long.class
    );

    final Optional<Customer> customer = underTest.selectCustomerById(id);

    BDDAssertions.assertThat(customer.isPresent())
        .isTrue();


    var expected = new Customer(id, "John", "email@email.com", 20);

    BDDAssertions.assertThat(customer.get())
        .usingRecursiveComparison()
        .ignoringFields("userDetailsApp")
        .isEqualTo(expected);

  }

  @Test
  void itShouldInsertCustomer() {
    var c = underTest.insertCustomer(new Customer(
        "Mary",
        "Mary@email.com",
        20
    ));

    BDDAssertions.assertThat(c.getId())
        .isNotNull();

  }

  @Test
  void givenExistingEmailWhenExistsByEmailItShouldReturnTrue() {
    final boolean isExist = underTest.existsByEmail("email@email.com");

    BDDAssertions.assertThat(isExist)
        .isTrue();
  }

  @Test
  void givenNonExistingEmailWhenExistsByEmailItShouldReturnTrue() {
    final boolean isExist = underTest.existsByEmail("emil@email.com");

    BDDAssertions.assertThat(isExist)
        .isFalse();
  }

  @Test
  void givenExistingIdWhenExistsByIdItShouldReturnTrue() {
    Long id = jdbcTemplate.queryForObject(
        """
            SELECT id
            FROM customer
            WHERE name = 'John'
            """,
        Long.class
    );
    boolean isExist = underTest.existsById(id);

    BDDAssertions.assertThat(isExist)
        .isTrue();
  }

  @Test
  void givenNonExistingIdWhenExistsByIdItShouldReturnFalse() {

    boolean isExist = underTest.existsById(-1L);

    BDDAssertions.assertThat(isExist)
        .isFalse();
  }

  @Test
  @Disabled
  void itShouldDeleteCustomerById() {
    // Given
    // When

    // Then
  }

  @Test
  @Disabled
  void itShouldUpdateCustomer() {
    // Given
    // When

    // Then
  }

  // TODO: test for db constraints

  @AfterEach
  void tearDown() {
  }
}