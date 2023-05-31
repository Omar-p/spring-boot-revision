package com.example.springbootrevision.journey;

import com.example.springbootrevision.customer.CustomerRepository;
import com.example.springbootrevision.customer.requests.CustomerPatchRequest;
import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.customer.requests.CustomerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
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

    final CustomerRegistrationRequest john = new CustomerRegistrationRequest("John", "jj@email.com", 22, "password");
    final String token = webTestClient.post()
        .uri(CUSTOMER_PATH)
        .bodyValue(john)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location")
        .returnResult(Void.class)
        .getResponseHeaders()
        .get(HttpHeaders.AUTHORIZATION).get(0);


    final CustomerRegistrationRequest johnh = new CustomerRegistrationRequest("Johnh", "jjh@email.com", 22, "password");
    final String token2 = webTestClient.post()
        .uri(CUSTOMER_PATH)
        .bodyValue(johnh)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location")
        .returnResult(Void.class)
        .getResponseHeaders()
        .get(HttpHeaders.AUTHORIZATION).get(0);
    Long id = customerRepository.findCustomerByEmail(johnh.email()).get().getId();

//    webTestClient.get()
//        .uri(CUSTOMER_PATH + "/" + id)
//        .header(HttpHeaders.AUTHORIZATION, token)
//        .exchange()
//        .expectStatus().isOk()
//        .expectBody()
//        .jsonPath("$.name").isEqualTo(john.name())
//        .jsonPath("$.email").isEqualTo(john.email())
//        .jsonPath("$.age").isEqualTo(john.age());


    webTestClient.post()
        .uri(CUSTOMER_PATH)
        .bodyValue(john)
        .exchange()
        .expectStatus().isEqualTo(HttpStatusCode.valueOf(409));

    webTestClient.patch()
        .uri(CUSTOMER_PATH + "/" + id)
        .header(HttpHeaders.AUTHORIZATION, token)
        .bodyValue(new CustomerPatchRequest(null, null, 55))
        .exchange()
        .expectStatus().isEqualTo(HttpStatusCode.valueOf(403));


//    webTestClient.delete()
//
//        .uri(CUSTOMER_PATH + "/" + id)
//        .header(HttpHeaders.AUTHORIZATION, token)
//        .exchange()
//        .expectStatus().isNoContent();

  }

}
