package com.carldev.alerta_recife.service;

import com.carldev.alerta_recife.dto.request.AuthRegisterRequest;
import com.carldev.alerta_recife.dto.request.LoginRequest;
import com.carldev.alerta_recife.dto.response.AuthRegisterResponse;
import com.carldev.alerta_recife.dto.response.AuthResponse;
import com.carldev.alerta_recife.dto.response.GoogleAuthUserProfileResponse;
import com.carldev.alerta_recife.entity.UserAuth;
import com.carldev.alerta_recife.exception.InvalidCredentialsException;
import com.carldev.alerta_recife.exception.UserAlreadyExistsException;
import com.carldev.alerta_recife.exception.UserNotVerifiedException;
import com.carldev.alerta_recife.mapper.AuthMapper;
import com.carldev.alerta_recife.repository.AuthRepository;
import com.carldev.alerta_recife.security.JwtTokenProvider;
import com.carldev.alerta_recife.utils.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthRegisterResponse registerUser(AuthRegisterRequest request) {

        if (authRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Usuário com e-mail cadastrado já existente");
        }

        UserAuth userAuth = AuthMapper.toEntity(request);

        String encodePassword = passwordEncoder.encode(request.password());
        userAuth.setPassword(encodePassword);
        userAuth.setIsVerified(false);
        userAuth.setRole(RoleType.USER);
        UserAuth saveUserAuth = authRepository.save(userAuth);

        return AuthMapper.toDto(saveUserAuth);
    }

    public AuthResponse UserLogin(LoginRequest request) {

        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(
                request.email(), request.password()
        );

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(userPass);
        } catch (DisabledException e) {
            throw new UserNotVerifiedException("Usuário não está ativo. Por favor, verifique seu e-mail.");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("E-mail ou senha incorretos.");
        }


        UserAuth userAuth = (UserAuth) authentication.getPrincipal();
        authRepository.updateLastLoginById(userAuth.getId(), Instant.now());
        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponse(userAuth.getUsername(), token);

    }

    public GoogleAuthUserProfileResponse getMyUserProfile(Authentication authentication)
            throws AccountNotFoundException {

        UserAuth user = (UserAuth) authentication.getPrincipal();

        if (user == null) {
            throw new AccountNotFoundException("Usuário não encontrado");
        }

        return AuthMapper.toGoogleAuthDto(user);
    }
}
