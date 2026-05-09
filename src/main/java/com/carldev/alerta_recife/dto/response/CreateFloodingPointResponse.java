package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;

public record CreateFloodingPointResponse(
        Long id,
        String street,
        String logger,
        String referencePoint,
        String description,
        String neighborhood,
        Double latitude,
        Double longitude,
        IntensityOfTheFlooding intensity,
        Integer confirmationVotes
) {

}
