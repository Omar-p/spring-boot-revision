package com.example.springbootrevision.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository("jdbc")
public class CustomerJdbcDataAccessService implements CustomerDao {


  private final JdbcTemplate jdbcTemplate;


  @Override
  public List<Customer> selectAllCustomers() {
    return jdbcTemplate.query(
        """
          SELECT id,
                 name,
                 email,
                 age
          FROM customer
            """,
        customerRowMapper()

    );
  }

  @Override
  public Optional<Customer> selectCustomerById(Long id) {
    final String sql =
        """
        SELECT id,
               name,
               email,
               age
        FROM customer
        WHERE id = ?
        """;

    final Customer customer = jdbcTemplate.queryForObject(
        sql,
        customerRowMapper(),
        id
    );

    return Optional.ofNullable(customer);
  }

  @Override
  public Customer insertCustomer(Customer customer) {

    var sql =
        """
        INSERT INTO customer(name, email, age)
        VALUES (?, ?, ?)
        """;
    jdbcTemplate.update(
        sql,
        customer.getName(),
        customer.getEmail(),
        customer.getAge()
    );

    return getByEmail(customer.getEmail());
  }

  @Override
  public boolean existsByEmail(String email) {
    final String sql =
        """
        SELECT EXISTS(
          SELECT 1
          FROM customer
          WHERE email = ?
        )
        """;

    return jdbcTemplate.queryForObject(
        sql,
        Boolean.class,
        email
    );
  }

  @Override
  public boolean existsById(Long id) {
    final String sql =
        """
        SELECT EXISTS(
          SELECT 1
          FROM customer
          WHERE id = ?
        )
        """;

    return jdbcTemplate.queryForObject(
        sql,
        Boolean.class,
        id
    );
  }

  @Override
  public void deleteCustomerById(Long id) {
    final String sql =
        """
        DELETE FROM customer
        WHERE id = ?
        """;

    jdbcTemplate.update(
        sql,
        id
    );

  }

  @Override
  public Customer updateCustomer(Customer customer) {
    final String sql =
        """
        UPDATE customer
        SET name = ?,
            email = ?,
            age = ?
        WHERE id = ?
        """;

    jdbcTemplate.update(
        sql,
        customer.getName(),
        customer.getEmail(),
        customer.getAge(),
        customer.getId()
    );

    return getByEmail(customer.getEmail());

  }

  private Customer getByEmail(String email) {
    final String sql =
        """
        SELECT id,
               name,
               email,
               age
        FROM customer
        WHERE email = ?
        """;

    return jdbcTemplate.queryForObject(
        sql,
        customerRowMapper(),
        email
    );
  }

  private static RowMapper<Customer> customerRowMapper() {
    return (resultSet, i) -> {
      var id = resultSet.getLong("id");
      var name = resultSet.getString("name");
      var email = resultSet.getString("email");
      var age = resultSet.getInt("age");
      return new Customer(id, name, email, age);
    };
  }
}
