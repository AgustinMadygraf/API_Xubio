package com.mady.api_xubio.repository;

import com.mady.api_xubio.model.MiEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiEmpresaRepository extends JpaRepository<MiEmpresa, String> {
    boolean existsByCuit(String cuit);
} 