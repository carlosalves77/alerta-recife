package com.carldev.alerta_recife.controller;

import com.carldev.alerta_recife.dto.request.AuthRegisterRequest;
import com.carldev.alerta_recife.dto.request.LoginRequest;
import com.carldev.alerta_recife.dto.response.AuthRegisterResponse;
import com.carldev.alerta_recife.dto.response.AuthResponse;
import com.carldev.alerta_recife.dto.response.GoogleAuthUserProfileResponse;
import com.carldev.alerta_recife.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> AuthRegister(
            @Valid
            @RequestBody AuthRegisterRequest request
    ) {

        AuthRegisterResponse response = authService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> AuthLogin(
            @Valid
            @RequestBody LoginRequest loginRequest
    ) {

        AuthResponse authResponse = authService.UserLogin(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<GoogleAuthUserProfileResponse> getMyProfile(Authentication authentication)
            throws AccountNotFoundException {

        GoogleAuthUserProfileResponse response = authService.getMyUserProfile(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
