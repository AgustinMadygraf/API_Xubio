package com.mady.api_xubio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "clientes", indexes = {
    @Index(name = "idx_cliente_nombre", columnList = "nombre")
})
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clienteId;
    
    @Column(nullable = false)
    private String nombre;
} 