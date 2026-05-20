package com.carldev.alerta_recife.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(

        @NotBlank(message = "Informe o e-mail")
        String email,

        @NotBlank(message = "Informe a senha")
        String password,

        @NotBlank(message = "Informe seu nome")
        String username

) {
}
