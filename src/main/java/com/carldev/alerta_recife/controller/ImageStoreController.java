package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.service.ImageStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/image")
public class ImageStoreController {

    private final ImageStoreService imageStoreService;

    public ImageStoreController(ImageStoreService imageStoreService) {
        this.imageStoreService = imageStoreService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
          @RequestParam("file") MultipartFile image
    ) throws IOException {

            try {
                if(image.isEmpty()) {
                    return ResponseEntity.badRequest().body("O arquivo não pode está vazio");
                }

                String uploadImage = imageStoreService.uploadImage(image);

                return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
            } catch (IOException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Erro ao processar o upload " + e.getLocalizedMessage());
            }

    }


}
