package com.carldev.alerta_recife.dto.request;

import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;
import org.locationtech.jts.geom.Point;

public record CreateFloodingPointResponse(
        String logger,
        String referencePoint,
        String neighborhood,
        Point coordinates,
        IntensityOfTheFlooding intensity,
        Integer confirmationVotes
) {
}
