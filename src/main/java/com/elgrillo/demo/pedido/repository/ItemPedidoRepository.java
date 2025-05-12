package com.elgrillo.demo.pedido.repository;

import com.elgrillo.demo.pedido.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad ItemPedido.
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    /**
     * Busca items por ID de pedido
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    /**
     * Busca un item específico de un pedido
     */
    Optional<ItemPedido> findByPedidoIdAndProductoId(Long pedidoId, Long productoId);
    
    /**
     * Elimina todos los items de un pedido
     */
    void deleteByPedidoId(Long pedidoId);
}
