package com.convocapro.auth;

import com.convocapro.user.ProfileType;

public record RegisterRequest(
        String fullName,
        String username,
        String email,
        String phone,
        String password,
        ProfileType profile
) {}
