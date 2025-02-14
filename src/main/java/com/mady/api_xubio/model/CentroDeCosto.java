package com.mady.api_xubio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "centros_de_costo")
@NoArgsConstructor
@AllArgsConstructor
public class CentroDeCosto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer centroDeCostoId;
    
    private String codigo;
    private String nombre;
} 