package com.carldev.alerta_recife.security.google_auth;

import com.carldev.alerta_recife.entity.UserAuth;
import com.carldev.alerta_recife.repository.AuthRepository;
import com.carldev.alerta_recife.security.JwtTokenProvider;
import com.carldev.alerta_recife.utils.AuthProvider;
import com.carldev.alerta_recife.utils.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
            response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        if (oAuth2User != null) {
            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String picture = oAuth2User.getAttribute("picture");

            UserAuth user = authRepository.findUserByEmail(email)
                    .orElseGet(() -> createGoogleUser(email, name, picture));

            user.setLastLoginAt(Instant.now());
            authRepository.save(user);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities());

            String token = jwtTokenProvider.generateToken(auth);

            ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofHours(8))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            getRedirectStrategy().sendRedirect(request, response,
                    "https://alertarecife.carldev.online/auth/callback");
        }

    }


    private UserAuth createGoogleUser(String email, String name, String picture) {

        UserAuth userAuth = UserAuth.builder()
                .email(email)
                .username(name)
                .password(null)
                .profilePicture(picture)
                .isVerified(true)
                .authProvider(AuthProvider.GOOGLE)
                .role(RoleType.USER)
                .build();

        return authRepository.save(userAuth);
    }

}
