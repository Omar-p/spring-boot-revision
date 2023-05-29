package com.example.springbootrevision.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JWTUtil {
  
  private static String SECRET_KEY = "TODO_STORE_AND_RETRIEVE_IT_FROM_ENVIRONMENT_VARIABLE";

  public String issueToken(String sub) {
    return issueToken(sub, Map.of());
  }

  public String issueToken(String sub, String ...scopes) {
    return issueToken(sub, Map.of("scopes", String.join(" ", scopes)));
  }
  public String issueToken(String sub, Map<String, Object> claims) {
    return Jwts
        .builder()
        .addClaims(claims)
        .setSubject(sub)
        .setIssuer("http://customers.us-east-1.elasticbeanstalk.com")
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(
            Date.from(
                Instant.now().plus(15, ChronoUnit.DAYS)
            )
        )
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String getSubject(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  public boolean isTokenValid(String token, String subject) {
    String sub = getSubject(token);
    return sub.equals(subject) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }
}
