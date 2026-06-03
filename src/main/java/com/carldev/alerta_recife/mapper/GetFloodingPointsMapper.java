package com.carldev.alerta_recife.mapper;

import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UserPublicResponse;
import com.carldev.alerta_recife.entity.FloodingPointImage;
import com.carldev.alerta_recife.entity.FloodingPoints;

import java.util.List;

public class GetFloodingPointsMapper {

    public static GetAllFloodingPointResponse toDto(FloodingPoints floodingPoints) {

        List<String> urls = floodingPoints.getImages().stream().map(FloodingPointImage::getImageUrl).toList();

        UserPublicResponse user = new UserPublicResponse(
                floodingPoints.getCreatedBy().getUsername(),
                floodingPoints.getCreatedBy().getProfilePicture()
        );

        return new GetAllFloodingPointResponse(
                floodingPoints.getId(),
                floodingPoints.getStreet(),
                floodingPoints.getLogger(),
                floodingPoints.getReferencePoint(),
                floodingPoints.getNeighborhood(),
                floodingPoints.getDescription(),
                floodingPoints.getCoordinates().getY(),
                floodingPoints.getCoordinates().getX(),
                floodingPoints.getIntensity(),
                floodingPoints.getConfirmationVotes(),
                urls,
                user
        );
    }


}
