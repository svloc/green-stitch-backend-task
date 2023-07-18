package com.green.stitch.jwt;

import com.green.stitch.service.UserDetailsImpl;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility {

   @Value("${jwtSecret}")
   private String jwtSecret;

   @Value("${jwtExpirationMs}")
   private int jwtExpirationMs;

   public String generateToken(Authentication authentication) {
      UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

      Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
      String roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

      return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
   }

   public String getUsernameFromToken(String token) {
      Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
      return claims.getSubject();
   }

   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   private Claims extractAllClaims(String token) {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
   }

   public Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   private Boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   public Boolean validateToken(String token, UserDetails userDetails) {
      final String username = getUsernameFromToken(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   public Boolean validateToken(String token) {
      try {
         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public Claims getClaim(String token) {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
   }

   public List<String> extractRoles(String token) {
      Claims claims = getClaim(token);
      String roles = (String) claims.get("roles");
      return Arrays.asList(roles.split(","));
   }
}
