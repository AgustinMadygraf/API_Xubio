package com.mady.api_xubio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "listas_precio", indexes = {
    @Index(name = "idx_lista_nombre", columnList = "nombre")
})
@NoArgsConstructor
@AllArgsConstructor
public class ListaPrecio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer listaPrecioId;
    
    @Column(nullable = false)
    private String nombre;
    
    private Boolean activo;
    
    private Integer tipo;
    
    @Column(name = "cantidad_items")
    private Integer cantidadItems;
    
    @Column(name = "ultima_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date ultimaActualizacion;
    
    private String descripcion;
    private Boolean esDefault;
    
    @Embedded
    private Moneda moneda;
    
    private Integer iva;
    private Boolean ocultarSinPrecio;
    
    @Embeddable
    @Data
    public static class Moneda {
        @Column(name = "moneda_id1")
        private Integer id1;
        
        @Column(name = "moneda_id2")
        private Integer id2;
    }
} 