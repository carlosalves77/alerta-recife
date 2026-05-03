package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.service.FloodingPointsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flooding")
public class FloodingPointsController {

    private final FloodingPointsService floodingPointsService;

    public FloodingPointsController(FloodingPointsService floodingPointsService) {
        this.floodingPointsService = floodingPointsService;
    }

    @PostMapping()
    public ResponseEntity<CreateFloodingPointResponse> createFloodingPoint(
            @Valid
            @RequestBody CreateFloodingPointRequest request
    ) {

        CreateFloodingPointResponse createFloodingPointResponse = floodingPointsService.createFloodingPoint(
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createFloodingPointResponse);
    }

    @GetMapping
    public ResponseEntity<List<GetAllFloodingPointResponse>> getAllFloodingPoint() {

        List<GetAllFloodingPointResponse> pointResponse = floodingPointsService.getAllFloodingPoints();

        return ResponseEntity.status(HttpStatus.OK).body(pointResponse);
    }
}
