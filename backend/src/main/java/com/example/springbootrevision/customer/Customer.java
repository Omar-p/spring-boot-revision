package com.example.springbootrevision.customer;

import com.example.springbootrevision.security.UserDetailsApp;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")
  @SequenceGenerator(
      name = "customer_id_sequence",
      sequenceName = "customer_id_sequence",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private Integer age;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(
      name = "user_details_app_id",
      referencedColumnName = "id",
      foreignKey = @ForeignKey(
          name = "user_details_app_id_fk"
      )
  )
  private UserDetailsApp userDetailsApp;

  public Customer(Long id, String name, String email, Integer age) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.age = age;
  }

  public Customer(String name, String email, Integer age) {
    this.name = name;
    this.email = email;
    this.age = age;
  }
}
