package com.convocapro.auth;

import com.convocapro.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        user = users.save(user);

        return response(user);
    }

    public AuthResponse login(LoginRequest req) {
        AppUser user = users.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.setTotalAccesses((user.getTotalAccesses() == null ? 0 : user.getTotalAccesses()) + 1);
        user = users.save(user);

        return response(user);
    }

    private AuthResponse response(AppUser user) {
        return new AuthResponse(
                jwt.token(user.getUsername()),
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
}
