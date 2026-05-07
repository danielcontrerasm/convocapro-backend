package com.convocapro.user;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AppUserRepository users;

    public UserController(AppUserRepository users) {
        this.users = users;
    }

    @GetMapping
    public List<Map<String, Object>> all() {
        return users.findAll().stream().map(this::toMap).toList();
    }

    @GetMapping("/{id}")
    public Map<String, Object> one(@PathVariable Long id) {
        return toMap(users.findById(id).orElseThrow());
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        List<AppUser> all = users.findAll();
        int totalAccesses = all.stream().mapToInt(u -> u.getTotalAccesses() == null ? 0 : u.getTotalAccesses()).sum();

        return Map.of(
                "totalUsers", all.size(),
                "activeUsers", all.stream().filter(u -> Boolean.TRUE.equals(u.getActive())).count(),
                "totalAccesses", totalAccesses
        );
    }

    @PutMapping("/{id}/retries")
    public Map<String, Object> updateRetries(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        AppUser user = users.findById(id).orElseThrow();
        if (body.containsKey("retryLimit")) user.setRetryLimit(body.get("retryLimit"));
        if (body.containsKey("retriesUsed")) user.setRetriesUsed(body.get("retriesUsed"));
        return toMap(users.save(user));
    }

    private Map<String, Object> toMap(AppUser u) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", u.getId());
        m.put("fullName", u.getFullName());
        m.put("username", u.getUsername());
        m.put("email", u.getEmail());
        m.put("phone", u.getPhone());
        m.put("role", u.getRole());
        m.put("profile", u.getProfile());
        m.put("retryLimit", u.getRetryLimit());
        m.put("retriesUsed", u.getRetriesUsed());
        m.put("totalAccesses", u.getTotalAccesses());
        m.put("active", u.getActive());
        m.put("hasActiveSession", hasActiveSession(u));
        m.put("activeSessionExpiresAt", u.getActiveSessionExpiresAt());
        return m;
    }

    private boolean hasActiveSession(AppUser u) {
        return u.getActiveSessionId() != null
                && u.getActiveSessionExpiresAt() != null
                && u.getActiveSessionExpiresAt().isAfter(LocalDateTime.now());
    }
}
