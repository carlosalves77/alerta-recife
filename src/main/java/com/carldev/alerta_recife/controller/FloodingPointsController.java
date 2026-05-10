package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.request.UpdateIntensityFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateConfirmationVotesFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateIntensityFloodingPointResponse;
import com.carldev.alerta_recife.service.FloodingPointsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PatchMapping("/{id}/intensity")
    public ResponseEntity<UpdateIntensityFloodingPointResponse> updateIntensityFloodingPoint(
            @Valid
            @PathVariable(value = "id") Long id,
            @RequestBody UpdateIntensityFloodingPointRequest intensity
    ) {

        UpdateIntensityFloodingPointResponse response =
                floodingPointsService.updateIntensityFloodingPoint(id, intensity.intensity());


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFloodingPointById(
            @PathVariable Long id
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(floodingPointsService.deleteFloodingPointById(id));
    }

    @PatchMapping("/{id}/votes")
    public ResponseEntity<UpdateConfirmationVotesFloodingPointResponse> updateConfirmationVotes(
            @PathVariable("id") Long id
    ) {

        UpdateConfirmationVotesFloodingPointResponse response = floodingPointsService.
                updateConfirmationVotesFloodingPointResponse(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllFloodingPoint() {
        floodingPointsService.deleteAllFloodingPoint();

        return ResponseEntity.status(HttpStatus.OK).body("Todos os pontos de alagamentos deletados");
    }

}
