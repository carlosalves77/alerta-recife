package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;

import java.util.List;

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
        Integer confirmationVotes,
        List<String> images
) {

}
