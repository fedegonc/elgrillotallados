package com.elgrillo.demo.producto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Producto que representa un tallado artesanal.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private String descripcion;
    
    private BigDecimal precio;
    
    private String imagenUrl;
    
    private Integer stock;
    
    private String material; // Por ejemplo: madera, piedra, etc.
    
    private String categoria; // Por ejemplo: decorativo, utilitario, etc.
    
    private String dimensiones; // Tamaño: "10x15x5 cm"
    
    private Double peso; // En kilogramos
    
    private Boolean destacado; // Para mostrar en página principal
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
}
