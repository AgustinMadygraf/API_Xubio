package com.mady.api_xubio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "mi_empresa", indexes = {
    @Index(name = "idx_empresa_cuit", columnList = "cuit")
})
@NoArgsConstructor
@AllArgsConstructor
public class MiEmpresa {
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Id
    @Column(nullable = false)
    private String nombreEmpresa;
    
    private Integer categoriaFiscal;
    private Integer tipoDeCuenta;
    private String ingresosBrutos;
    
    @Temporal(TemporalType.DATE)
    private Date fechaInicioActividad;
    
    private String direccion;
    private Integer pais;
    private Integer provincia;
    private Integer localidad;
    private String telefono;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    private Integer facturam;
    
    @Pattern(regexp = "^[0-9]{11}$", message = "El CUIT debe tener 11 dígitos")
    @Column(unique = true)
    private String cuit;
} 