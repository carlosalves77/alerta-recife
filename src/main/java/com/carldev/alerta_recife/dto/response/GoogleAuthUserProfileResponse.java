package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.RoleType;

import java.time.LocalDateTime;
import java.util.UUID;

public record GoogleAuthUserProfileResponse(
        UUID id,
        String email,
        String username,
        String profilePicture,
        RoleType role,
        LocalDateTime createdAt
) {
}
