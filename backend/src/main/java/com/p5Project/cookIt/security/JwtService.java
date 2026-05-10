package com.p5Project.cookIt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String userId) {
        return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        }

        catch (JwtException e) {
            return false;
        }
    }
}