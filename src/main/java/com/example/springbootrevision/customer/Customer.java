package com.example.springbootrevision.customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer", uniqueConstraints = {
    @UniqueConstraint(name = "customer_email_unique", columnNames = "email")
})
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
  @SequenceGenerator(
      name = "customer_sequence",
      sequenceName = "customer_sequence",
      allocationSize = 1)
  private Long id;

  private String name;

  private String email;

  private Integer age;
}
