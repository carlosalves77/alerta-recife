package com.carldev.alerta_recife.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateFloodingPointRequest(

        @NotBlank(message = "A rua é obrigatório")
        String street,

        @NotBlank(message = "O nome do relator é obrigatório")
        String logger,

        String referencePoint,

        String description,

        @NotBlank(message = "O bairro é obrigatório")
        String neighborhood,

        @NotNull(message = "As coordenadas geográficas são obrigatórias")
        Double latitude,

        @NotNull(message = "As coordenadas geográficas são obrigatórias")
        Double longitude,


        @NotBlank(message = "A intensidade do alagamento é obrigatória")
        String intensity,

        List<String> images
) {
}
