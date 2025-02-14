package com.mady.api_xubio.service;

import com.mady.api_xubio.config.XubioConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class XubioAuthService {
    private static final Logger log = LoggerFactory.getLogger(XubioAuthService.class);

    private final RestTemplate restTemplate;
    private final XubioConfig config;
    private String currentToken;
    private long tokenExpirationTime;

    public XubioAuthService(RestTemplate restTemplate, XubioConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public synchronized String getValidToken() {
        if (currentToken == null || System.currentTimeMillis() >= tokenExpirationTime) {
            refreshToken();
        }
        return currentToken;
    }

    private void refreshToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(config.getClientId(), config.getClientSecret());

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            log.debug("Intentando obtener token de: {}", config.getTokenEndpoint());
            
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                config.getTokenEndpoint(),
                request,
                TokenResponse.class
            );

            if (response.getBody() != null) {
                TokenResponse tokenResponse = response.getBody();
                currentToken = tokenResponse.getAccessToken();
                // Establecer el tiempo de expiración 5 minutos antes del tiempo real
                tokenExpirationTime = System.currentTimeMillis() + 
                    ((tokenResponse.getExpiresIn() - 300) * 1000L);
                log.info("Token actualizado exitosamente");
                log.debug("Token type: {}", tokenResponse.getTokenType());
            } else {
                log.error("No se pudo obtener el token de acceso - respuesta vacía");
                throw new RuntimeException("No se pudo obtener el token de acceso");
            }
        } catch (Exception e) {
            log.error("Error al refrescar el token: {}", e.getMessage());
            throw new RuntimeException("Error al obtener el token de acceso: " + e.getMessage(), e);
        }
    }

    public static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("token_type")
        private String tokenType;
        
        @JsonProperty("expires_in")
        private Integer expiresIn;

        // Getters y setters
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
} 