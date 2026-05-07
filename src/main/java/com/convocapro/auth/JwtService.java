package com.convocapro.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-minutes}")
    private long expirationMinutes;

    private SecretKey key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String token(String username, String sessionId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .id(sessionId)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMinutes * 60_000))
                .signWith(key)
                .compact();
    }

    public LocalDateTime expirationDate() {
        long expiresAt = System.currentTimeMillis() + expirationMinutes * 60_000;
        return LocalDateTime.ofInstant(new Date(expiresAt).toInstant(), ZoneId.systemDefault());
    }

    public TokenDetails parse(String token) {
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new TokenDetails(claims.getSubject(), claims.getId());
    }

    public record TokenDetails(String username, String sessionId) {}
}
