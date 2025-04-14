package com.mady.api_xubio.controller;

import com.mady.api_xubio.model.*;
import com.mady.api_xubio.service.XubioDataService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xubio")
public class XubioController {
    private final XubioDataService dataService;

    public XubioController(XubioDataService dataService) {
        this.dataService = dataService;
    }

    // Endpoints para Cliente
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(dataService.saveCliente(cliente));
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(dataService.getAllClientes());
    }

    // Endpoints para MiEmpresa
    @PostMapping("/empresas")
    public ResponseEntity<MiEmpresa> createEmpresa(@RequestBody MiEmpresa empresa) {
        return ResponseEntity.ok(dataService.saveMiEmpresa(empresa));
    }

    @GetMapping("/empresas/{nombre}")
    public ResponseEntity<MiEmpresa> getEmpresa(@PathVariable String nombre) {
        return dataService.getMiEmpresaByNombre(nombre)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/empresas")
    public ResponseEntity<List<MiEmpresa>> getAllEmpresas() {
        return ResponseEntity.ok(dataService.findAll());
    }

    // Endpoints para CentroDeCosto
    @PostMapping("/centros-costo")
    public ResponseEntity<CentroDeCosto> createCentroDeCosto(@RequestBody CentroDeCosto centroDeCosto) {
        return ResponseEntity.ok(dataService.saveCentroDeCosto(centroDeCosto));
    }

    @GetMapping("/centros-costo")
    public ResponseEntity<List<CentroDeCosto>> getAllCentrosDeCosto() {
        return ResponseEntity.ok(dataService.getAllCentrosDeCosto());
    }

    // Endpoints para ListaPrecio
    @PostMapping("/listas-precio")
    public ResponseEntity<ListaPrecio> createListaPrecio(@RequestBody ListaPrecio listaPrecio) {
        return ResponseEntity.ok(dataService.saveListaPrecio(listaPrecio));
    }

    @GetMapping("/listas-precio")
    public ResponseEntity<List<ListaPrecio>> getAllListasPrecios() {
        return ResponseEntity.ok(dataService.getAllListasPrecios());
    }

    // Endpoints para Vendedor
    @PostMapping("/vendedores")
    public ResponseEntity<Void> createVendedor(@Valid @RequestBody Vendedor vendedor) {
        dataService.saveVendedor(vendedor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vendedores")
    public ResponseEntity<List<Vendedor>> getAllVendedores() {
        return ResponseEntity.ok(dataService.getAllVendedores());
    }
} 