package com.elgrillo.demo.pedido.repository;

import com.elgrillo.demo.pedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Pedido.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    /**
     * Busca pedidos por cliente ID
     */
    List<Pedido> findByClienteId(Long clienteId);
    
    /**
     * Busca el carrito activo de un cliente
     */
    Optional<Pedido> findByClienteIdAndEstado(Long clienteId, Pedido.EstadoPedido estado);
    
    /**
     * Busca pedidos por número de referencia
     */
    Optional<Pedido> findByNumeroReferencia(String numeroReferencia);
}
