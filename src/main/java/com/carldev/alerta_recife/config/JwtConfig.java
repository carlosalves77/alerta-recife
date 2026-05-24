package com.carldev.alerta_recife.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {

    @Value("${JWT_SECRET}")
    private String secret;

    private long expirationMs = 86400000;
    private String headerName = "Authorization";
    private String tokenPrefix = "Bearer ";
}
