package com.elgrillo.demo.pedido.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Pedido que representa una orden de compra.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long clienteId;
    
    private String numeroReferencia;
    
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime fechaActualizacion;
    
    private EstadoPedido estado;
    
    private BigDecimal subtotal;
    
    private BigDecimal costoEnvio;
    
    private BigDecimal impuestos;
    
    private BigDecimal total;
    
    private String nombreEnvio;
    
    private String direccionEnvio;
    
    private String ciudadEnvio;
    
    private String departamentoEnvio;
    
    private String codigoPostalEnvio;
    
    private String telefonoEnvio;
    
    private String metodoPago;
    
    private String notasAdicionales;
    
    @OneToMany(mappedBy = "pedidoId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Transient // Mantenemos @Transient para compatibilidad con el código existente
    private List<ItemPedido> items = new ArrayList<>();
    
    /**
     * Calcular los totales del pedido.
     */
    public void calcularTotales() {
        this.subtotal = items.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Aplicar impuestos y costos de envío si están definidos
        if (this.impuestos == null) {
            this.impuestos = BigDecimal.ZERO;
        }
        if (this.costoEnvio == null) {
            this.costoEnvio = BigDecimal.ZERO;
        }
        
        this.total = this.subtotal.add(this.impuestos).add(this.costoEnvio);
    }
    
    /**
     * Añadir un item al pedido.
     */
    public void agregarItem(ItemPedido item) {
        items.add(item);
        calcularTotales();
    }
    
    /**
     * Eliminar un item del pedido.
     */
    public void eliminarItem(Long itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calcularTotales();
    }
    
    /**
     * Estados posibles de un pedido.
     */
    public enum EstadoPedido {
        CARRITO,
        PENDIENTE,
        PROCESANDO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }
}
