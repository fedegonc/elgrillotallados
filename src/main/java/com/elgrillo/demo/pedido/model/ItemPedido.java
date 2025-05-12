package com.elgrillo.demo.pedido.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidad ItemPedido que representa un producto dentro de un pedido.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items_pedido")
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long pedidoId;
    
    private Long productoId;
    
    private String productoNombre;
    
    private Integer cantidad;
    
    private BigDecimal precioUnitario;
    
    private BigDecimal subtotal;
    
    /**
     * Calcular el subtotal (precio unitario * cantidad).
     */
    public void calcularSubtotal() {
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
    }
}
