package com.carldev.alerta_recife.mapper;

import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.entity.FloodingPoints;

public class GetFloodingPointsMapper {

    public static GetAllFloodingPointResponse toDto(FloodingPoints floodingPoints) {

        return new GetAllFloodingPointResponse(
                floodingPoints.getId(),
                floodingPoints.getLogger(),
                floodingPoints.getReferencePoint(),
                floodingPoints.getNeighborhood(),
                floodingPoints.getCoordinates().getX(),
                floodingPoints.getCoordinates().getY(),
                floodingPoints.getIntensity(),
                floodingPoints.getConfirmationVotes()
        );
    }


}
