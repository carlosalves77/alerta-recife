package com.carldev.alerta_recife.dto.response;

import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;


public record CreateFloodingPointRequest (
        String logger,
        String referencePoint,
        String neighborhood,
        Point coordinates,
        String intensity,
        boolean active,
        LocalDateTime registryDate,
        Integer confirmationVotes
) {
}
