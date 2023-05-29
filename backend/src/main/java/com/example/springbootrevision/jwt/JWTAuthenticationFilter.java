package com.example.springbootrevision.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = authorizationHeader.substring("Bearer ".length());


    final String subject = jwtUtil.getSubject(token);
    if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      final UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
      if (jwtUtil.isTokenValid(token, subject)) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      }
    }
    filterChain.doFilter(request, response);
  }
}
