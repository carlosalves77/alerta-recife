package com.carldev.alerta_recife.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Informe o e-mail do usuário")
        String email,

        @NotBlank(message = "Informe a senha")
        String password
) {
}
