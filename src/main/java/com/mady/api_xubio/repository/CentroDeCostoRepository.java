package com.mady.api_xubio.repository;

import com.mady.api_xubio.model.CentroDeCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDeCostoRepository extends JpaRepository<CentroDeCosto, Integer> {
} 