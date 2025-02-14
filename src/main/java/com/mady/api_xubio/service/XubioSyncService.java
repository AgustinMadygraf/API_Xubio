package com.mady.api_xubio.service;

import com.mady.api_xubio.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.mady.api_xubio.config.XubioConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.Collections;

@Service
public class XubioSyncService {
    private static final Logger log = LoggerFactory.getLogger(XubioSyncService.class);
    
    private final RestTemplate restTemplate;
    private final XubioConfig config;
    private final XubioDataService dataService;

    public XubioSyncService(
        RestTemplate restTemplate,
        XubioConfig config,
        XubioDataService dataService
    ) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.dataService = dataService;
    }

    @Transactional
    public void syncAllData(String token) {
        try {
            log.info("Iniciando sincronización de datos...");
            
            syncClientes(token);
            syncCentrosDeCosto(token);
            syncListasPrecios(token);
            syncVendedores(token);
            
            log.info("Sincronización completada exitosamente");
        } catch (Exception e) {
            log.error("Error durante la sincronización: {}", e.getMessage());
            throw e;
        }
    }

    private void syncClientes(String token) {
        String url = config.getBaseApiUrl() + "clienteBean";
        ResponseEntity<Cliente[]> response = makeAuthenticatedRequest(url, token, Cliente[].class);
        if (response.getBody() != null) {
            for (Cliente cliente : response.getBody()) {
                dataService.saveCliente(cliente);
            }
        }
    }

    private void syncCentrosDeCosto(String token) {
        String url = config.getBaseApiUrl() + "centroDeCostoBean";
        ResponseEntity<CentroDeCosto[]> response = makeAuthenticatedRequest(url, token, CentroDeCosto[].class);
        if (response.getBody() != null) {
            for (CentroDeCosto centro : response.getBody()) {
                dataService.saveCentroDeCosto(centro);
            }
        }
    }

    private void syncListasPrecios(String token) {
        String url = config.getBaseApiUrl() + "listaPrecioBean";
        ResponseEntity<ListaPrecio[]> response = makeAuthenticatedRequest(url, token, ListaPrecio[].class);
        if (response.getBody() != null) {
            for (ListaPrecio lista : response.getBody()) {
                dataService.saveListaPrecio(lista);
            }
        }
    }

    private void syncVendedores(String token) {
        String url = config.getBaseApiUrl() + "vendedorBean";
        ResponseEntity<Vendedor[]> response = makeAuthenticatedRequest(url, token, Vendedor[].class);
        if (response.getBody() != null) {
            for (Vendedor vendedor : response.getBody()) {
                dataService.saveVendedor(vendedor);
            }
        }
    }

    private <T> ResponseEntity<T> makeAuthenticatedRequest(String url, String token, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        log.debug("Haciendo petición a: {}", url);
        
        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            responseType
        );
    }
} 