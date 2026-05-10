package com.carldev.alerta_recife.mapper;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateConfirmationVotesFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateIntensityFloodingPointResponse;
import com.carldev.alerta_recife.entity.FloodingPoints;
import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;

public class FloodingPointsMapper {

    public static CreateFloodingPointResponse toDto(FloodingPoints floodingPoints) {

        return new CreateFloodingPointResponse(
                floodingPoints.getId(),
                floodingPoints.getStreet(),
                floodingPoints.getLogger(),
                floodingPoints.getReferencePoint(),
                floodingPoints.getDescription(),
                floodingPoints.getNeighborhood(),
                floodingPoints.getCoordinates().getY(),
                floodingPoints.getCoordinates().getX(),
                floodingPoints.getIntensity(),
                floodingPoints.getConfirmationVotes()
        );
    }

    public static FloodingPoints toEntity(CreateFloodingPointRequest request) {

        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = factory.createPoint(new Coordinate(request.longitude(), request.latitude()));

        return FloodingPoints.builder()
                .street(request.street())
                .logger(request.logger())
                .description(request.description())
                .referencePoint(request.referencePoint())
                .neighborhood(request.neighborhood())
                .coordinates(point)
                .intensity(IntensityOfTheFlooding.valueOf(request.intensity()))
                .registryDate(LocalDateTime.now())
                .active(true)
                .confirmationVotes(0)
                .build();

    }

    public static UpdateIntensityFloodingPointResponse toDtoUpdate(FloodingPoints floodingPoints) {
        return new UpdateIntensityFloodingPointResponse(
                floodingPoints.getId(),
                floodingPoints.getStreet(),
                floodingPoints.getLogger(),
                floodingPoints.getReferencePoint(),
                floodingPoints.getNeighborhood(),
                floodingPoints.getCoordinates().getX(),
                floodingPoints.getCoordinates().getY(),
                floodingPoints.getIntensity(),
                floodingPoints.getConfirmationVotes()
        );
    }

    public static UpdateConfirmationVotesFloodingPointResponse toConfirmVotesUpdateDto(
            FloodingPoints floodingPoints) {
        return new UpdateConfirmationVotesFloodingPointResponse(
                floodingPoints.getId(),
                floodingPoints.getStreet(),
                floodingPoints.getLogger(),
                floodingPoints.getReferencePoint(),
                floodingPoints.getNeighborhood(),
                floodingPoints.getDescription(),
                floodingPoints.getCoordinates().getX(),
                floodingPoints.getCoordinates().getY(),
                floodingPoints.getIntensity(),
                floodingPoints.getConfirmationVotes()
        );
    }

}
