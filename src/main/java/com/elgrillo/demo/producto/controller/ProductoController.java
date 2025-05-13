package com.elgrillo.demo.producto.controller;

import com.elgrillo.demo.producto.model.Producto;
import com.elgrillo.demo.producto.repository.ProductoRepository;
import com.elgrillo.demo.producto.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Controlador para gestionar las vistas relacionadas con productos/tallados.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    /**
     * Muestra la lista de todos los productos
     */
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.obtenerTodos());
        model.addAttribute("title", "Catálogo");
        return "productos/lista";
    }
    
    /**
     * Ruta alternativa para mostrar todos los productos
     */
    @GetMapping("/lista")
    public String listarProductosAlternativo(Model model) {
        return listarProductos(model);
    }
    
    /**
     * Muestra los detalles de un producto específico
     */
    @GetMapping("/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        productoService.buscarPorId(id).ifPresent(producto -> {
            model.addAttribute("producto", producto);
            model.addAttribute("title", producto.getNombre());
        });
        return "productos/detalle";
    }
    
    /**
     * Muestra productos filtrados por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public String listarPorCategoria(@PathVariable String categoria, Model model) {
        model.addAttribute("productos", productoService.buscarPorCategoria(categoria));
        model.addAttribute("categoriaActual", categoria);
        model.addAttribute("title", "Tallados - " + categoria);
        return "productos/lista";
    }
    
    /**
     * Busca productos por nombre
     */
    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam String q, Model model) {
        model.addAttribute("productos", productoService.buscarPorNombre(q));
        model.addAttribute("consulta", q);
        model.addAttribute("title", "Resultados para: " + q);
        return "productos/lista";
    }
}
