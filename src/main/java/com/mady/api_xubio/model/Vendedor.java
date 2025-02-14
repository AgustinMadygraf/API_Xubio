package com.mady.api_xubio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "vendedores", indexes = {
    @Index(name = "idx_vendedor_nombre_apellido", columnList = "nombre, apellido")
})
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    
    @Id
    private Long vendedorId;
    
    private String nombre;
    private String apellido;
    private Integer activo;
} 