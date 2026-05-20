package com.carldev.alerta_recife.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {

        Map<String, String> error = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(errors ->
                error.put(errors.getField(), errors.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidSpatialDataException.class)
    public ResponseEntity<ErrorResponseDTO> invalidSpatialDataException(InvalidSpatialDataException e) {

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(responseDTO.Status()).body(responseDTO);
    }

    @ExceptionHandler(FloodingPointIdException.class)
    public ResponseEntity<ErrorResponseDTO> handleIfFloodingPointNotFoundException
            (FloodingPointIdException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorResponseDTO.Status()).body(errorResponseDTO);
    }

    @ExceptionHandler(NearbyFloodingPointException.class)
    public ResponseEntity<ErrorResponseDTO> handleIfHasNearbyFloodingPoints(NearbyFloodingPointException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorResponseDTO.Status()).body(errorResponseDTO);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleIfUserAlreadyExists(UserAlreadyExistsException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorResponseDTO.Status()).body(errorResponseDTO);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ErrorResponseDTO> handleIfUserIsNotVerified(UserNotVerifiedException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorResponseDTO.Status()).body(errorResponseDTO);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleIfUserCredentialsAreValid(InvalidCredentialsException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(errorResponseDTO.Status()).body(errorResponseDTO);
    }
}
