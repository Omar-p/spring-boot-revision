package com.example.springbootrevision.customer;

import com.example.springbootrevision.customer.requests.CustomerRegistrationRequest;
import com.example.springbootrevision.exception.DelegatedAuthEntryPoint;
import com.example.springbootrevision.jwt.JWTUtil;
import com.example.springbootrevision.security.CorsConfig;
import com.example.springbootrevision.security.SecurityConfig;
import com.example.springbootrevision.security.SecurityFilterChainConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@AutoConfigureRestDocs
@WebMvcTest(CustomerController.class)
@Import({SecurityConfig.class, SecurityFilterChainConfig.class, CorsConfig.class, DelegatedAuthEntryPoint.class})
class CustomerControllerTest {


  @Autowired
  MockMvc mockMvc;

  @MockBean
  JWTUtil jwtUtil;

  @MockBean
  CustomerService customerService;

  @MockBean
  UserDetailsService userDetailsService;

  private static final String CUSTOMER_PATH = "/api/v1/customers";

  @Test
  void itShouldRegisterNewCustomer() throws Exception {


    // Given
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOiJST0xFX1VTRVIiLCJzdWIiOiJvbWFyQGVtYWlsLmNvbSIsImlzcyI6Imh0dHA6Ly9jdXN0b21lcnMudXMtZWFzdC0xLmVsYXN0aWNiZWFuc3RhbGsuY29tIiwiaWF0IjoxNjg1Njk2MTA2LCJleHAiOjE2ODY5OTIxMDZ9.My5e1LhYkMi2nnyXejNl95G_p6jig3BhMIERarCHi9M";
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(
        "Omar",
        "omar@email.com",
          25,
        "password"
    );
    BDDMockito.given(customerService.addNewCustomer(request))
        .willReturn(1L);
    BDDMockito.given(jwtUtil.issueToken(request.email(), "ROLE_USER"))
        .willReturn(token);

    // When
    mockMvc.perform(RestDocumentationRequestBuilders.post(CUSTOMER_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(
        """
       {
          "name": "Omar",
          "email": "omar@email.com",
          "age": 25,
          "password": "password"
       }  \s
        """
        ))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.is("/api/v1/customers/1")))
        .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, Matchers.is("Bearer " + token)))
        .andDo(
            document("register-new-customer",
              requestHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description("type of the body, e.g. JSON")
              ),
              responseHeaders(
                headerWithName(HttpHeaders.LOCATION).description("location of the newly created customer"),
                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT token for the newly created customer")
              )
            )
        );


    // Then
  }
}