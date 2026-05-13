package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;

import java.util.List;

public record GetAllFloodingPointResponse(
        Long id,
        String street,
        String logger,
        String referencePoint,
        String neighborhood,
        String description,
        Double latitude,
        Double longitude,
        IntensityOfTheFlooding intensity,
        Integer confirmationVotes,
        List<String> images
) {

}
