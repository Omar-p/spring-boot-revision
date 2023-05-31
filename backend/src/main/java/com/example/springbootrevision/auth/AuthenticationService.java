package com.example.springbootrevision.auth;

import com.example.springbootrevision.jwt.JWTUtil;
import com.example.springbootrevision.security.UserDetailsApp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;


  public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
    final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        authenticationRequest.email(),
        authenticationRequest.password()
    ));

    final UserDetails details = (UserDetailsApp) authenticate.getPrincipal();
    final String token = jwtUtil.issueToken(details.getUsername(),
        details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));

    return ResponseEntity.ok()
        .header("Authorization", "Bearer " + token)
        .build();
  }
}
