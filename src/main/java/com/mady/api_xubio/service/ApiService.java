package com.mady.api_xubio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mady.api_xubio.config.XubioConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(ApiService.class);
    
    private final RestTemplate restTemplate;
    private final XubioAuthService authService;
    private final XubioConfig config;
    private final ObjectMapper objectMapper;
    private final XubioSyncService syncService;

    @Value("${xubio.sync.enabled:true}")
    private boolean syncEnabled;

    public ApiService(
        RestTemplate restTemplate, 
        XubioAuthService authService, 
        XubioConfig config,
        ObjectMapper objectMapper,
        XubioSyncService syncService
    ) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.config = config;
        this.objectMapper = objectMapper;
        this.syncService = syncService;
    }

    @Scheduled(initialDelayString = "${xubio.sync.initial-delay-ms:60000}", 
              fixedRateString = "${xubio.sync.interval-ms:86400000}")
    public void fetchDataAndStore() {
        if (!syncEnabled) {
            log.debug("Sincronización deshabilitada por configuración");
            return;
        }
        try {
            String accessToken = authService.getValidToken();
            if (accessToken != null) {
                syncService.syncAllData(accessToken);
            } else {
                log.error("No se pudo obtener un token válido para la sincronización");
            }
        } catch (Exception e) {
            log.error("Error durante la sincronización: {}", e.getMessage());
        }
    }

    private List<Map<String, String>> loadMenuOptions() {
        try (InputStream inputStream = getClass().getResourceAsStream("/services/menu_config.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: /services/menu_config.json");
            }
            Map<String, Object> config = objectMapper.readValue(inputStream, new TypeReference<>() {});
            return (List<Map<String, String>>) config.get("menu_options");
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void fetchAndStoreData(String endpoint, String dataType) {
        String url = config.getBaseApiUrl() + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getValidToken());

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            processAndStoreData(response.getBody(), dataType);
        } else {
            System.out.println("Failed to fetch data from " + endpoint);
        }
    }

    private void processAndStoreData(String data, String dataType) {
        // Implementar el procesamiento y almacenamiento de datos según el dataType
        System.out.println("Processing data of type: " + dataType);
        System.out.println("Data: " + data);
    }
}
