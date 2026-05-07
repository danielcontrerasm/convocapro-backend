package com.convocapro.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.STUDENT;

    @Enumerated(EnumType.STRING)
    private ProfileType profile = ProfileType.DEMO;

    private Integer retryLimit = 3;
    private Integer retriesUsed = 0;
    private Integer totalAccesses = 0;
    private Boolean active = true;
    private String activeSessionId;
    private LocalDateTime activeSessionExpiresAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public ProfileType getProfile() { return profile; }
    public Integer getRetryLimit() { return retryLimit; }
    public Integer getRetriesUsed() { return retriesUsed; }
    public Integer getTotalAccesses() { return totalAccesses; }
    public Boolean getActive() { return active; }
    public String getActiveSessionId() { return activeSessionId; }
    public LocalDateTime getActiveSessionExpiresAt() { return activeSessionExpiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(UserRole role) { this.role = role; }
    public void setProfile(ProfileType profile) { this.profile = profile; }
    public void setRetryLimit(Integer retryLimit) { this.retryLimit = retryLimit; }
    public void setRetriesUsed(Integer retriesUsed) { this.retriesUsed = retriesUsed; }
    public void setTotalAccesses(Integer totalAccesses) { this.totalAccesses = totalAccesses; }
    public void setActive(Boolean active) { this.active = active; }
    public void setActiveSessionId(String activeSessionId) { this.activeSessionId = activeSessionId; }
    public void setActiveSessionExpiresAt(LocalDateTime activeSessionExpiresAt) { this.activeSessionExpiresAt = activeSessionExpiresAt; }
}
