package com.carldev.alerta_recife.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateIntensityFloodingPointRequest(

        @NotNull(message = "Informe o id do alagamento")
        Long id,

        @NotBlank(message = "A intensidade do alagamento é obrigatória")
        String intensity
) {
}
