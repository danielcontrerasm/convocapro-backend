package com.convocapro.auth;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        service.logout(authorizationHeader);
        return Map.of("message", "Logged out");
    }
}
