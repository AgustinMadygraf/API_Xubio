package com.mady.api_xubio.controller;

import com.mady.api_xubio.service.XubioSyncService;
import com.mady.api_xubio.service.XubioAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/sync")
public class XubioSyncController {
    private static final Logger log = LoggerFactory.getLogger(XubioSyncController.class);
    
    private final XubioSyncService syncService;
    private final XubioAuthService authService;

    public XubioSyncController(XubioSyncService syncService, XubioAuthService authService) {
        this.syncService = syncService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> syncData() {
        try {
            String token = authService.getValidToken();
            if (token != null) {
                syncService.syncAllData(token);
                return ResponseEntity.ok().build();
            } else {
                log.error("No se pudo obtener un token válido para la sincronización");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error("Error durante la sincronización: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 