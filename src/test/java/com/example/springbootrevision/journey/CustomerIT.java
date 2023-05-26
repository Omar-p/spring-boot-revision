package com.example.springbootrevision.journey;

import com.example.springbootrevision.customer.CustomerRepository;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class CustomerIT {

  @Autowired
  WebTestClient webTestClient;
  @Autowired
  CustomerRepository customerRepository;

  private static final String CUSTOMER_PATH = "/api/v1/customers";

  @BeforeEach
  void setUp() {
    customerRepository.deleteAll();
  }


  @Test
  void canCrudCustomer() {

    final CustomerRegistrationRequest john = new CustomerRegistrationRequest("John", "jj@email.com", 22);
    webTestClient.post()
        .uri(CUSTOMER_PATH)
        .bodyValue(john)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location");

    Long id = customerRepository.findCustomerByEmail(john.email()).get().getId();

    webTestClient.get()
        .uri(CUSTOMER_PATH + "/" + id)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo(john.name())
        .jsonPath("$.email").isEqualTo(john.email())
        .jsonPath("$.age").isEqualTo(john.age());

    webTestClient.post()
        .uri(CUSTOMER_PATH)
        .bodyValue(john)
        .exchange()
        .expectStatus().isEqualTo(HttpStatusCode.valueOf(409));


    webTestClient.delete()
        .uri(CUSTOMER_PATH + "/" + id)
        .exchange()
        .expectStatus().isNoContent();

  }

}
