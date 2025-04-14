package com.mady.api_xubio.service;

import com.mady.api_xubio.model.*;
import com.mady.api_xubio.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class XubioDataService {
    private static final Logger log = LoggerFactory.getLogger(XubioDataService.class);
    
    private final ClienteRepository clienteRepository;
    private final MiEmpresaRepository miEmpresaRepository;
    private final CentroDeCostoRepository centroDeCostoRepository;
    private final ListaPrecioRepository listaPrecioRepository;
    private final VendedorRepository vendedorRepository;

    public XubioDataService(
        ClienteRepository clienteRepository,
        MiEmpresaRepository miEmpresaRepository,
        CentroDeCostoRepository centroDeCostoRepository,
        ListaPrecioRepository listaPrecioRepository,
        VendedorRepository vendedorRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.miEmpresaRepository = miEmpresaRepository;
        this.centroDeCostoRepository = centroDeCostoRepository;
        this.listaPrecioRepository = listaPrecioRepository;
        this.vendedorRepository = vendedorRepository;
    }

    // Métodos para Cliente
    @Transactional
    public Cliente saveCliente(@Valid Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Métodos para MiEmpresa
    @Transactional
    public MiEmpresa saveMiEmpresa(@Valid MiEmpresa empresa) {
        if (miEmpresaRepository.existsByCuit(empresa.getCuit())) {
            throw new IllegalArgumentException("Ya existe una empresa con ese CUIT");
        }
        return miEmpresaRepository.save(empresa);
    }

    public Optional<MiEmpresa> getMiEmpresaByNombre(String nombre) {
        return miEmpresaRepository.findById(nombre);
    }

    public List<MiEmpresa> findAll() {
        return miEmpresaRepository.findAll();
    }

    // Métodos para CentroDeCosto
    @Transactional
    public CentroDeCosto saveCentroDeCosto(@Valid CentroDeCosto centroDeCosto) {
        return centroDeCostoRepository.save(centroDeCosto);
    }

    public List<CentroDeCosto> getAllCentrosDeCosto() {
        return centroDeCostoRepository.findAll();
    }

    // Métodos para ListaPrecio
    @Transactional
    public ListaPrecio saveListaPrecio(@Valid ListaPrecio listaPrecio) {
        return listaPrecioRepository.save(listaPrecio);
    }

    public List<ListaPrecio> getAllListasPrecios() {
        return listaPrecioRepository.findAll();
    }

    // Métodos para Vendedor
    @Transactional
    public void saveVendedor(@Valid Vendedor vendedor) {
        try {
            Optional<Vendedor> existingVendedor = vendedorRepository.findById(vendedor.getVendedorId());
            if (existingVendedor.isPresent()) {
                Vendedor current = existingVendedor.get();
                current.setNombre(vendedor.getNombre());
                current.setApellido(vendedor.getApellido());
                current.setActivo(vendedor.getActivo());
                vendedorRepository.save(current);
                log.debug("Vendedor actualizado: {}", vendedor.getVendedorId());
            } else {
                vendedorRepository.save(vendedor);
                log.debug("Nuevo vendedor guardado: {}", vendedor.getVendedorId());
            }
        } catch (Exception e) {
            log.error("Error al guardar vendedor {}: {}", vendedor.getVendedorId(), e.getMessage());
            throw e;
        }
    }

    public List<Vendedor> getAllVendedores() {
        return vendedorRepository.findAll();
    }
} 