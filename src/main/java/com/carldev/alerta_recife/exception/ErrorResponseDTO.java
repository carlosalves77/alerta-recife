package com.carldev.alerta_recife.exception;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int Status,
        String message,
        LocalDateTime timestamp
) {
}
