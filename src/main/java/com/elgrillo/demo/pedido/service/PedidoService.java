package com.elgrillo.demo.pedido.service;

import com.elgrillo.demo.pedido.model.ItemPedido;
import com.elgrillo.demo.pedido.model.Pedido;
import com.elgrillo.demo.pedido.model.Pedido.EstadoPedido;
import com.elgrillo.demo.pedido.repository.ItemPedidoRepository;
import com.elgrillo.demo.pedido.repository.PedidoRepository;
import com.elgrillo.demo.producto.model.Producto;
import com.elgrillo.demo.producto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la gestión de pedidos.
 * Contiene la lógica de negocio relacionada con los pedidos.
 */
@Service
@RequiredArgsConstructor
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProductoService productoService;
    
    /**
     * Obtiene todos los pedidos
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        pedidos.forEach(this::cargarItemsPedido);
        return pedidos;
    }
    
    /**
     * Busca un pedido por su ID
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(this::cargarItemsPedido);
    }
    
    /**
     * Busca pedidos por cliente ID
     */
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorClienteId(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        pedidos.forEach(this::cargarItemsPedido);
        return pedidos;
    }
    
    /**
     * Busca el carrito activo de un cliente
     */
    @Transactional
    public Pedido buscarCarrito(Long clienteId) {
        return pedidoRepository.findByClienteIdAndEstado(clienteId, EstadoPedido.CARRITO)
                .map(this::cargarItemsPedido)
                .orElseGet(() -> crearCarrito(clienteId));
    }
    
    /**
     * Crea un nuevo carrito para un cliente
     */
    private Pedido crearCarrito(Long clienteId) {
        Pedido carrito = new Pedido();
        carrito.setClienteId(clienteId);
        carrito.setEstado(EstadoPedido.CARRITO);
        carrito.setFechaCreacion(LocalDateTime.now());
        carrito.setFechaActualizacion(LocalDateTime.now());
        Pedido savedCarrito = pedidoRepository.save(carrito);
        return cargarItemsPedido(savedCarrito);
    }
    
    /**
     * Carga los items de un pedido
     */
    private Pedido cargarItemsPedido(Pedido pedido) {
        List<ItemPedido> items = itemPedidoRepository.findByPedidoId(pedido.getId());
        pedido.setItems(items);
        pedido.calcularTotales();
        return pedido;
    }
    
    /**
     * Agrega un producto al carrito
     */
    @Transactional
    public Pedido agregarAlCarrito(Long clienteId, Long productoId, Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        
        Pedido carrito = buscarCarrito(clienteId);
        Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return agregarItemAlCarrito(carrito, producto, cantidad);
    }
    
    /**
     * Agrega un item al carrito
     */
    private Pedido agregarItemAlCarrito(Pedido carrito, Producto producto, Integer cantidad) {
        Optional<ItemPedido> existingItem = itemPedidoRepository.findByPedidoIdAndProductoId(carrito.getId(), producto.getId());
        
        if (existingItem.isPresent()) {
            // Actualizar cantidad del item existente
            ItemPedido item = existingItem.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.calcularSubtotal();
            itemPedidoRepository.save(item);
        } else {
            // Crear nuevo item
            ItemPedido nuevoItem = new ItemPedido();
            nuevoItem.setPedidoId(carrito.getId());
            nuevoItem.setProductoId(producto.getId());
            nuevoItem.setProductoNombre(producto.getNombre());
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.calcularSubtotal();
            itemPedidoRepository.save(nuevoItem);
        }
        
        return cargarItemsPedido(carrito);
    }
    
    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    @Transactional
    public Pedido actualizarCantidadEnCarrito(Long clienteId, Long itemId, Integer cantidad) {
        if (cantidad <= 0) {
            return eliminarDelCarrito(clienteId, itemId);
        }
        
        Pedido carrito = buscarCarrito(clienteId);
        
        itemPedidoRepository.findById(itemId)
                .filter(item -> item.getPedidoId().equals(carrito.getId()))
                .ifPresent(item -> {
                    item.setCantidad(cantidad);
                    item.calcularSubtotal();
                    itemPedidoRepository.save(item);
                });
        
        return cargarItemsPedido(carrito);
    }
    
    /**
     * Elimina un producto del carrito
     */
    @Transactional
    public Pedido eliminarDelCarrito(Long clienteId, Long itemId) {
        Pedido carrito = buscarCarrito(clienteId);
        itemPedidoRepository.deleteById(itemId);
        return cargarItemsPedido(carrito);
    }
    
    /**
     * Vacía el carrito
     */
    @Transactional
    public Pedido vaciarCarrito(Long clienteId) {
        Pedido carrito = buscarCarrito(clienteId);
        itemPedidoRepository.deleteByPedidoId(carrito.getId());
        return cargarItemsPedido(carrito);
    }
    
    /**
     * Finaliza un pedido (pasar de carrito a pedido pendiente)
     */
    @Transactional
    public Pedido finalizarPedido(Long clienteId, Pedido datosEnvio) {
        Pedido carrito = buscarCarrito(clienteId);
        
        // Copiar datos de envío
        carrito.setNombreEnvio(datosEnvio.getNombreEnvio());
        carrito.setDireccionEnvio(datosEnvio.getDireccionEnvio());
        carrito.setCiudadEnvio(datosEnvio.getCiudadEnvio());
        carrito.setDepartamentoEnvio(datosEnvio.getDepartamentoEnvio());
        carrito.setCodigoPostalEnvio(datosEnvio.getCodigoPostalEnvio());
        carrito.setTelefonoEnvio(datosEnvio.getTelefonoEnvio());
        carrito.setMetodoPago(datosEnvio.getMetodoPago());
        carrito.setNotasAdicionales(datosEnvio.getNotasAdicionales());
        
        // Generar número de referencia único
        carrito.setNumeroReferencia(generarNumeroReferencia());
        carrito.setEstado(EstadoPedido.PENDIENTE);
        carrito.setFechaActualizacion(LocalDateTime.now());
        
        return pedidoRepository.save(carrito);
    }
    
    /**
     * Genera un número de referencia único para el pedido
     */
    private String generarNumeroReferencia() {
        return "EGT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Cambia el estado de un pedido
     */
    @Transactional
    public Optional<Pedido> cambiarEstadoPedido(Long pedidoId, EstadoPedido nuevoEstado) {
        return pedidoRepository.findById(pedidoId)
                .map(pedido -> {
                    pedido.setEstado(nuevoEstado);
                    pedido.setFechaActualizacion(LocalDateTime.now());
                    Pedido savedPedido = pedidoRepository.save(pedido);
                    return cargarItemsPedido(savedPedido);
                });
    }
}
