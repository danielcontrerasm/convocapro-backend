package com.convocapro.auth;

import com.convocapro.user.ProfileType;
import com.convocapro.user.UserRole;

public record AuthResponse(
        String token,
        Long userId,
        String username,
        String fullName,
        ProfileType profile,
        UserRole role,
        Integer retryLimit,
        Integer retriesUsed,
        Integer totalAccesses
) {}
