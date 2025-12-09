package com.cmed.medic.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmed.medic.auth.dto.UserDTO;

@Component
public class JwtUtil {
    private final String secret;
    private final long expirationMs;

    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Key getKey() {
        return key;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public String generateToken(UserDTO user) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.id());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.username())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("id", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
