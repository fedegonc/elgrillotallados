package com.elgrillo.demo.producto.repository;

import com.elgrillo.demo.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Producto.
 * Proporciona métodos para acceder a los datos de productos en la base de datos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Busca productos por nombre (contiene, case-insensitive)
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca productos por categoría
     */
    List<Producto> findByCategoria(String categoria);
    
    /**
     * Encuentra productos destacados para mostrar en la página principal
     */
    List<Producto> findByDestacadoTrue();
}
