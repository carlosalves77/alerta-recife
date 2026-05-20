package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.RoleType;

import java.time.LocalDateTime;

public record AuthRegisterResponse(
        String email,
        String username,
        LocalDateTime createdAt,
        RoleType role
) {
}
