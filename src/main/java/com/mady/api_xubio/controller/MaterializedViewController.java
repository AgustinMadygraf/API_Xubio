package com.mady.api_xubio.controller;

import com.mady.api_xubio.service.MaterializedViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/views")
public class MaterializedViewController {

    private final MaterializedViewService viewService;

    public MaterializedViewController(MaterializedViewService viewService) {
        this.viewService = viewService;
    }

    @GetMapping("/centros-costo/summary")
    public ResponseEntity<List<Map<String, Object>>> getCentroDeCostoSummary() {
        return ResponseEntity.ok(viewService.getCentroDeCostoSummary());
    }

    @GetMapping("/listas-precio/summary")
    public ResponseEntity<List<Map<String, Object>>> getListaPreciosSummary() {
        return ResponseEntity.ok(viewService.getListaPreciosSummary());
    }

    @GetMapping("/refresh/centros-costo")
    public ResponseEntity<Void> refreshCentroDeCostoView() {
        viewService.refreshCentroDeCostoView();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh/listas-precio")
    public ResponseEntity<Void> refreshListaPreciosView() {
        viewService.refreshListaPreciosView();
        return ResponseEntity.ok().build();
    }
} 