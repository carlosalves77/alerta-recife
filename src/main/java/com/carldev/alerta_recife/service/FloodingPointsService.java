package com.carldev.alerta_recife.service;

import com.carldev.alerta_recife.dto.request.CreateFloodingPointRequest;
import com.carldev.alerta_recife.dto.response.CreateFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.GetAllFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateConfirmationVotesFloodingPointResponse;
import com.carldev.alerta_recife.dto.response.UpdateIntensityFloodingPointResponse;
import com.carldev.alerta_recife.entity.FloodingPointImage;
import com.carldev.alerta_recife.entity.FloodingPoints;
import com.carldev.alerta_recife.entity.UserAuth;
import com.carldev.alerta_recife.exception.FloodingPointIdException;
import com.carldev.alerta_recife.exception.InvalidSpatialDataException;
import com.carldev.alerta_recife.exception.NearbyFloodingPointException;
import com.carldev.alerta_recife.mapper.FloodingPointsMapper;
import com.carldev.alerta_recife.mapper.GetFloodingPointsMapper;
import com.carldev.alerta_recife.repository.AuthRepository;
import com.carldev.alerta_recife.repository.FloodingPointsRepository;
import com.carldev.alerta_recife.utils.IntensityOfTheFlooding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FloodingPointsService {

    private final FloodingPointsRepository floodingPointsRepository;
    private final AuthRepository authRepository;
    private final ImageStoreService imageStoreService;

    public FloodingPointsService(FloodingPointsRepository floodingPointsRepository,
                                 AuthRepository authRepository,
                                 ImageStoreService imageStoreService) {
        this.floodingPointsRepository = floodingPointsRepository;
        this.authRepository = authRepository;

        this.imageStoreService = imageStoreService;
    }

    @Transactional
    public CreateFloodingPointResponse createFloodingPoint(
            CreateFloodingPointRequest request, List<MultipartFile> files, Authentication authentication)
            throws IOException {

        UserAuth userAuth = (UserAuth) authentication.getPrincipal();

        if (userAuth == null) {
            throw new IOException("Usuário é nulo");
        }

        if (request.latitude() == null || request.longitude() == null) {
            throw new InvalidSpatialDataException("As coordenadas informadas são invalidas");
        }

        Optional<FloodingPoints> pointNearby = floodingPointsRepository.findNearByActive(
                request.latitude(), request.longitude(), 20.0
        );

        if (pointNearby.isPresent()) {
            throw new NearbyFloodingPointException("Já existe um alerta ativo muito próximo a este local.");
        }


        FloodingPoints floodingPoints = FloodingPointsMapper.toEntity(request);

        if (files != null && !files.isEmpty()) {

            if (files.size() > 6) throw new RuntimeException("Máximo de 6 fotos permidito");

            for (MultipartFile file : files) {
                String fileName =
                        "https://cdn.carldev.online/alerta-recife/" + imageStoreService.uploadImage(file);

                FloodingPointImage pointImage = new FloodingPointImage();
                pointImage.setImageUrl(fileName);

                floodingPoints.addImage(pointImage);
            }
        }

        UserAuth user = authRepository.getReferenceById(userAuth.getId());
        floodingPoints.setCreatedBy(user);
        FloodingPoints saveFloodingPoint = floodingPointsRepository.save(floodingPoints);

        return FloodingPointsMapper.toDto(saveFloodingPoint);
    }

    @Transactional(readOnly = true)
    public List<GetAllFloodingPointResponse> getAllFloodingPoints() {

        List<FloodingPoints> floodingPointsList = floodingPointsRepository.findFloodingPoints();

        return floodingPointsList.stream().map(GetFloodingPointsMapper::toDto
        ).collect(Collectors.toList());
    }

    public UpdateIntensityFloodingPointResponse updateIntensityFloodingPoint(
            Long id, String intensity) {

        FloodingPoints floodingPoints = floodingPointsRepository.findById(id)
                .orElseThrow(() -> new FloodingPointIdException("Ponto de alagamento não encontrado"));

        floodingPoints.setIntensity(IntensityOfTheFlooding.valueOf(intensity));
        floodingPointsRepository.save(floodingPoints);

        return FloodingPointsMapper.toDtoUpdate(floodingPoints);
    }

    @Transactional(readOnly = true)
    public List<GetAllFloodingPointResponse> getFloodingPointsByUserId(UUID userId)
            throws IllegalAccessException {

        List<FloodingPoints> floodingPointsList = floodingPointsRepository.findFloodingPointsByUserId(
                userId
        );
        return floodingPointsList.stream().map(GetFloodingPointsMapper::toDto
        ).collect(Collectors.toList());
    }


    @Transactional
    public String deleteFloodingPoint(Long id, UUID userId) throws IllegalAccessException {


        if (id == null) {
            throw new FloodingPointIdException("Informe o id do ponto de alagamento");
        }

        FloodingPoints floodingPoints = floodingPointsRepository.findByIdAndCreatedById(
                id, userId
        ).orElseThrow(
                () -> new FloodingPointIdException("Ponto de alagamento não encontrado")
        );

        List<String> imagesToDelete = floodingPoints.getImages().stream().map(FloodingPointImage::getImageUrl)
                .toList();


        if (!imagesToDelete.isEmpty()) {
            imageStoreService.deleteImage(imagesToDelete);
        }

        floodingPointsRepository.deleteById(floodingPoints.getId());

        return "Id: " + floodingPoints.getId() + " foi deletado";
    }

    public UpdateConfirmationVotesFloodingPointResponse updateConfirmationVotesFloodingPointResponse(
            Long id
    ) {
        if (!floodingPointsRepository.existsById(id)) {
            throw new FloodingPointIdException("Não foi encontrado um ponto de alagamento");
        }
        floodingPointsRepository.incrementConfirmationVotes(id);

        FloodingPoints updateFloodingPoint = floodingPointsRepository.findById(id).orElse(null);

        assert updateFloodingPoint != null;
        return FloodingPointsMapper.toConfirmVotesUpdateDto(updateFloodingPoint);
    }

    public void deleteAllFloodingPoint() {
        floodingPointsRepository.deleteAll();
    }

}
