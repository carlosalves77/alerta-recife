package com.carldev.alerta_recife.mapper;

import com.carldev.alerta_recife.dto.request.AuthRegisterRequest;
import com.carldev.alerta_recife.dto.response.AuthRegisterResponse;
import com.carldev.alerta_recife.dto.response.GoogleAuthUserProfileResponse;
import com.carldev.alerta_recife.entity.UserAuth;

public class AuthMapper {

    public static AuthRegisterResponse toDto(UserAuth userAuth) {

        return new AuthRegisterResponse(
                userAuth.getEmail(),
                userAuth.getUsername(),
                userAuth.getCreatedAt(),
                userAuth.getRole()
        );
    }

    public static UserAuth toEntity(AuthRegisterRequest authRegisterRequest) {

        return UserAuth.builder()
                .email(authRegisterRequest.email())
                .password(authRegisterRequest.password())
                .username(authRegisterRequest.username())
                .build();
    }

    public static GoogleAuthUserProfileResponse toGoogleAuthDto(UserAuth userAuth) {

        return new GoogleAuthUserProfileResponse(
                userAuth.getId(),
                userAuth.getEmail(),
                userAuth.getUsername(),
                userAuth.getProfilePicture(),
                userAuth.getRole(),
                userAuth.getCreatedAt()
        );
    }


}
