package com.carldev.alerta_recife.service;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.entity.FloodingPoints;
import com.carldev.alerta_recife.exception.InvalidSpatialDataException;
import com.carldev.alerta_recife.mapper.CreateFloodingPointsMapper;
import com.carldev.alerta_recife.mapper.GetFloodingPointsMapper;
import com.carldev.alerta_recife.repository.FloodingPointsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FloodingPointsService {

    private final FloodingPointsRepository floodingPointsRepository;

    public FloodingPointsService(FloodingPointsRepository floodingPointsRepository) {
        this.floodingPointsRepository = floodingPointsRepository;

    }

    @Transactional
    public CreateFloodingPointResponse createFloodingPoint(CreateFloodingPointRequest request) {

        if (request.latitude() == null || request.longitude() == null) {
            throw new InvalidSpatialDataException("As coordenadas informadas são invalidas");
        }

        Optional<FloodingPoints> pointNearby = floodingPointsRepository.findNearByActive(
                request.latitude(), request.longitude(), 20.0
        );

        if (pointNearby.isPresent()) {
            FloodingPoints existing = pointNearby.get();
            existing.setConfirmationVotes(+1);
            return CreateFloodingPointsMapper.toDto(floodingPointsRepository.save(existing));
        }

        FloodingPoints floodingPoints = CreateFloodingPointsMapper.toEntity(request);

        FloodingPoints saveFloodingPoint = floodingPointsRepository.save(floodingPoints);

        return CreateFloodingPointsMapper.toDto(saveFloodingPoint);
    }

    @Transactional(readOnly = true)
    public List<GetAllFloodingPointResponse> getAllFloodingPoints() {

        List<FloodingPoints> floodingPointsList = floodingPointsRepository.findFloodingPoints();

        return floodingPointsList.stream().map(GetFloodingPointsMapper::toDto
        ).collect(Collectors.toList());
    }

}
