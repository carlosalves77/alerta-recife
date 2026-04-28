package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointRequest;
import com.carldev.alerta_recife.service.FloodingPointsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flooding")
public class FloodingPointsController {

    private final FloodingPointsService floodingPointsService;

    public FloodingPointsController(FloodingPointsService floodingPointsService) {
        this.floodingPointsService = floodingPointsService;
    }

    @PostMapping()
    public ResponseEntity<CreateFloodingPointResponse> createFloodingPoint(
            @RequestBody CreateFloodingPointRequest request
    ) {

        CreateFloodingPointResponse createFloodingPointResponse = floodingPointsService.createFloodingPoint(
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createFloodingPointResponse);
    }
}
