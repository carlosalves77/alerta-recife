package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.request.UpdateIntensityFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateConfirmationVotesFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateIntensityFloodingPointResponse;
import com.carldev.alerta_recife.entity.UserAuth;
import com.carldev.alerta_recife.service.FloodingPointsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/flooding")
public class FloodingPointsController {

    private final FloodingPointsService floodingPointsService;

    public FloodingPointsController(FloodingPointsService floodingPointsService) {
        this.floodingPointsService = floodingPointsService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateFloodingPointResponse> createFloodingPoint(
            @Valid
            @RequestPart("data") CreateFloodingPointRequest request,
            @RequestPart("file") List<MultipartFile> files,
            Authentication authentication
    ) throws IOException {

        CreateFloodingPointResponse createFloodingPointResponse = floodingPointsService.createFloodingPoint(
                request, files, authentication
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
    public ResponseEntity<String> deleteFloodingPoint(
            @PathVariable Long id,
            Authentication authentication
    ) throws IllegalAccessException {

        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        if (userAuth == null) {
            throw new IllegalAccessException("Usuário invalido");
        }

        String floodingPoints = floodingPointsService.deleteFloodingPoint(id,
                userAuth.getId());

        return ResponseEntity.status(HttpStatus.OK).body(floodingPoints);
    }

    @GetMapping("/user")
    public ResponseEntity<List<GetAllFloodingPointResponse>> getFloodingPointsByUser(
            Authentication authentication
    ) throws IllegalAccessException {

        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        if (userAuth == null) {
            throw new IllegalAccessException("Usuário invalido");
        }

        List<GetAllFloodingPointResponse> listFloodingPoints =
                floodingPointsService.getFloodingPointsByUserId(userAuth.getId());


        return ResponseEntity.status(HttpStatus.OK).body(listFloodingPoints);
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
