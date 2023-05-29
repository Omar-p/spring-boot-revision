package com.example.springbootrevision.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(
    name = "user_details_app",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "user_details_app_email_key",
            columnNames = "email"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class UserDetailsApp implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_details_app_id_sequence")
  @SequenceGenerator(
      name = "user_details_app_id_sequence",
      sequenceName = "user_details_app_id_sequence",
      allocationSize = 1,
      initialValue = 1
  )
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private boolean locked;

  public UserDetailsApp(Long id, String email, String password, boolean locked) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.locked = locked;
  }

  public UserDetailsApp(String email, String password, boolean locked) {
    this.email = email;
    this.password = password;
    this.locked = locked;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return locked;
  }

  @Override
  public boolean isAccountNonLocked() {
    return locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return locked;
  }

  @Override
  public boolean isEnabled() {
    return locked;
  }
}
