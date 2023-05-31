package com.example.springbootrevision.customer;

import com.example.springbootrevision.security.UserDetailsApp;
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

    var insertUserDetails = """
       
        INSERT INTO user_details_app(email, password, enabled) VALUES (?, ?, ?)
""";



    var sql =
        """
        INSERT INTO customer(name, email, age, user_details_app_id)
        VALUES (?, ?, ?, ?)
        """;

    jdbcTemplate.update(
        insertUserDetails,
        customer.getEmail(),
        "password",
        true
    );

    long userDetailsId = selectUserDetailsByEmail(customer.getEmail()).getId();

    jdbcTemplate.update(
        sql,
        customer.getName(),
        customer.getEmail(),
        customer.getAge(),
        userDetailsId
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

  @Override
  public Optional<Customer> selectCustomerByEmail(String email) {
    return Optional.ofNullable(getByEmail(email));
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

  private UserDetailsApp selectUserDetailsByEmail(String email) {
    final String sql =
        """
        SELECT id,
               email,
               password,
               enabled
        FROM user_details_app
        WHERE email = ?
        """;

    return jdbcTemplate.queryForObject(
        sql,
        userDetailsRowMapper(),
        email
    );
  }

  private static RowMapper<UserDetailsApp> userDetailsRowMapper() {
    return (resultSet, i) -> {
      var id = resultSet.getLong("id");
      var email = resultSet.getString("email");
      var password = resultSet.getString("password");
      var enabled = resultSet.getBoolean("enabled");
      return new UserDetailsApp(id, email, password, enabled);
    };
  }

  private RowMapper<Customer> customerRowMapper() {
    return (resultSet, i) -> {
      var id = resultSet.getLong("id");
      var name = resultSet.getString("name");
      var email = resultSet.getString("email");
      var age = resultSet.getInt("age");

      final Customer customer = new Customer(id, name, email, age);

      customer.setUserDetailsApp(selectUserDetailsByEmail(email));

      return customer;
    };
  }
}
