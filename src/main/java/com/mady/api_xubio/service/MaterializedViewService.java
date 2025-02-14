package com.mady.api_xubio.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MaterializedViewService {
    
    private final JdbcTemplate jdbcTemplate;

    public MaterializedViewService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Scheduled(fixedRate = 3600000) // Actualiza cada hora
    public void refreshCentroDeCostoView() {
        jdbcTemplate.execute("""
            REPLACE INTO mv_centros_costo_summary
            SELECT 
                c.centro_de_costo_id,
                c.codigo,
                c.nombre,
                COUNT(*) as total_registros,
                NOW() as ultima_actualizacion
            FROM centros_de_costo c
            GROUP BY c.centro_de_costo_id, c.codigo, c.nombre
        """);
    }

    @Transactional
    @Scheduled(fixedRate = 3600000) // Actualiza cada hora
    public void refreshListaPreciosView() {
        jdbcTemplate.execute("""
            REPLACE INTO mv_lista_precios_summary
            SELECT 
                l.lista_precio_id,
                l.nombre,
                l.activo,
                l.tipo,
                COUNT(*) as cantidad_items,
                NOW() as ultima_actualizacion
            FROM listas_precio l
            GROUP BY l.lista_precio_id, l.nombre, l.activo, l.tipo
        """);
    }

    public List<Map<String, Object>> getCentroDeCostoSummary() {
        return jdbcTemplate.queryForList("""
            SELECT * FROM mv_centros_costo_summary
            ORDER BY nombre
        """);
    }

    public List<Map<String, Object>> getListaPreciosSummary() {
        return jdbcTemplate.queryForList("""
            SELECT * FROM mv_lista_precios_summary
            WHERE activo = true
            ORDER BY nombre
        """);
    }
} 