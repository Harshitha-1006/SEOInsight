package com.seoanalyzer.seo_tool.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;         // read from application.properties

    @Value("${jwt.expiration}")
    private long expiration;      // 86400000ms = 24 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Called at login — creates token with email inside
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Called on every request — extracts email from token
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Validates token — not expired + matches user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }
}