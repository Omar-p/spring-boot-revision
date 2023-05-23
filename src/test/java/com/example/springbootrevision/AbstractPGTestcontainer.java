package com.example.springbootrevision;

import org.assertj.core.api.BDDAssertions;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractPGTestcontainer {

  @Container
  @ServiceConnection
  protected static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:15-alpine")
          .withUsername("test")
          .withPassword("test")
          .withDatabaseName("customer");


  @BeforeAll
  static void canApplyDbMigrations() {
    Flyway.configure().dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
        )
        .load()
        .migrate();

  }

}
