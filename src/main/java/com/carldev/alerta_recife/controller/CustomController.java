package com.carldev.alerta_recife.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", statusCode);

        switch (statusCode) {
            case 404 -> {
                errorResponse.put("error", "Not Found");
                errorResponse.put("message", "Endpoint não existe");
            }
            case 401 -> {
                errorResponse.put("error", "Unauthorized");
                errorResponse.put("message", "Sem permissão");
            }
            case 403 -> {
                errorResponse.put("error", "Forbidden");
                errorResponse.put("message", "Acesso negado");
            }
            case 405 -> {
                errorResponse.put("error", "Method Not Allowed");
                errorResponse.put("message", "Método HTTP não permitido para este endpoint");
            }
            default -> {
                errorResponse.put("error", "Internal Server Error");
                errorResponse.put("message", "Ocorreu um erro inesperado no servidor");
            }
        }
        return ResponseEntity.status(statusCode).body(errorResponse);
    }

}
