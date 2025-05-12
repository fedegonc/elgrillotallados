package com.elgrillo.demo.producto.service;

import com.elgrillo.demo.producto.model.Producto;
import com.elgrillo.demo.producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de productos/tallados.
 * Contiene la lógica de negocio relacionada con los productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    /**
     * Obtiene todos los productos
     */
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }
    
    /**
     * Busca un producto por su ID
     */
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    /**
     * Guarda un nuevo producto o actualiza uno existente
     */
    public Producto guardar(Producto producto) {
        // Si es un producto nuevo, establecer la fecha de creación
        if (producto.getId() == null) {
            producto.setFechaCreacion(LocalDateTime.now());
        }
        
        // Siempre actualizar la fecha de actualización
        producto.setFechaActualizacion(LocalDateTime.now());
        
        return productoRepository.save(producto);
    }
    
    /**
     * Elimina un producto por su ID
     */
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Busca productos por categoría
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    /**
     * Obtiene productos destacados para mostrar en la página principal
     */
    public List<Producto> obtenerDestacados() {
        return productoRepository.findByDestacadoTrue();
    }
}
