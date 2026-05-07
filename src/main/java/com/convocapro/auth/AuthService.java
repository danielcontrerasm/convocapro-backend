package com.convocapro.auth;

import com.convocapro.user.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(AppUserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (users.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        AppUser user = new AppUser();
        user.setFullName(req.fullName());
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPhone(req.phone());
        user.setPasswordHash(encoder.encode(req.password()));
        user.setProfile(req.profile() == null ? ProfileType.DEMO : req.profile());
        startSession(user);
        user = users.save(user);

        return response(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        AppUser user = users.findByUsernameForUpdate(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (hasActiveSession(user)) {
            throw new IllegalStateException("User already has an active session");
        }

        user.setTotalAccesses((user.getTotalAccesses() == null ? 0 : user.getTotalAccesses()) + 1);
        startSession(user);
        user = users.save(user);

        return response(user);
    }

    @Transactional
    public void logout(String authorizationHeader) {
        String token = bearerToken(authorizationHeader);
        JwtService.TokenDetails details;

        try {
            details = jwt.parse(token);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid token");
        }

        AppUser user = users.findByUsernameForUpdate(details.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (details.sessionId() != null && details.sessionId().equals(user.getActiveSessionId())) {
            user.setActiveSessionId(null);
            user.setActiveSessionExpiresAt(null);
            users.save(user);
        }
    }

    private AuthResponse response(AppUser user) {
        return new AuthResponse(
                jwt.token(user.getUsername(), user.getActiveSessionId()),
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getProfile(),
                user.getRole(),
                user.getRetryLimit(),
                user.getRetriesUsed(),
                user.getTotalAccesses()
        );
    }

    private void startSession(AppUser user) {
        user.setActiveSessionId(UUID.randomUUID().toString());
        user.setActiveSessionExpiresAt(jwt.expirationDate());
    }

    private boolean hasActiveSession(AppUser user) {
        return user.getActiveSessionId() != null
                && user.getActiveSessionExpiresAt() != null
                && user.getActiveSessionExpiresAt().isAfter(LocalDateTime.now());
    }

    private String bearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must use Bearer token");
        }
        return authorizationHeader.substring("Bearer ".length()).trim();
    }
}
