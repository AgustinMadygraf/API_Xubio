package com.mady.api_xubio.repository;

import com.mady.api_xubio.model.ListaPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaPrecioRepository extends JpaRepository<ListaPrecio, Integer> {
} 