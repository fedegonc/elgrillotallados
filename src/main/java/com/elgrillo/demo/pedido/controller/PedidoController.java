package com.elgrillo.demo.pedido.controller;

import com.elgrillo.demo.cliente.model.Cliente;
import com.elgrillo.demo.cliente.service.ClienteService;
import com.elgrillo.demo.pedido.model.Pedido;
import com.elgrillo.demo.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    /**
     * Obtiene el ID del cliente autenticado actualmente
     */
    private Long getClienteId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        Optional<Cliente> cliente = clienteService.buscarPorEmail(auth.getName());
        return cliente.map(Cliente::getId).orElse(null);
    }

    /* ---------- 1. CARRITO (GET muestra – POST muta) -------------------- */

    @GetMapping("/carrito")
    public String carrito(Model m) {
        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        m.addAttribute("carrito", pedidoService.buscarCarrito(clienteId));
        m.addAttribute("title", "Carrito de Compras");
        return "pedidos/carrito";
    }

    /** action = add | update | remove | clear */
    @PostMapping("/carrito")
    @ResponseStatus(HttpStatus.SEE_OTHER) // 303 → redirección
    public String mutarCarrito(
            @RequestParam String action,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(defaultValue = "1") Integer cantidad) {

        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        switch (action) {
            case "add"    -> pedidoService.agregarAlCarrito(clienteId, productoId, cantidad);
            case "update" -> pedidoService.actualizarCantidadEnCarrito(clienteId, itemId, cantidad);
            case "remove" -> pedidoService.eliminarDelCarrito(clienteId, itemId);
            case "clear"  -> pedidoService.vaciarCarrito(clienteId);
        }
        return "redirect:/pedidos/carrito";
    }

    /* ---------- 2. CHECKOUT (GET formulario – POST confirmar) ----------- */

    @GetMapping("/checkout")
    public String checkout(Model m) {
        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        var carrito = pedidoService.buscarCarrito(clienteId);
        if (carrito.getItems().isEmpty()) {
            m.addAttribute("error", "Tu carrito está vacío");
        }

        m.addAttribute("carrito", carrito);
        m.addAttribute("pedido", new Pedido());
        m.addAttribute("title", "Finalizar Compra");
        return "pedidos/checkout";
    }

    @PostMapping("/checkout")
    public String confirmar(@ModelAttribute Pedido datos, Model m) {
        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        try {
            var pedido = pedidoService.finalizarPedido(clienteId, datos);
            return "redirect:/pedidos/" + pedido.getId();
        } catch (Exception e) {
            m.addAttribute("error", "Error al procesar: " + e.getMessage());
            return checkout(m);
        }
    }

    /* ---------- 3. PEDIDO INDIVIDUAL & HISTORIAL ----------------------- */

    @GetMapping("/{id}")
    public String verPedido(@PathVariable Long id, Model m) {
        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        return pedidoService.buscarPorId(id)
                .filter(p -> p.getClienteId().equals(clienteId))
                .map(p -> {
                    m.addAttribute("pedido", p);
                    m.addAttribute("title", "Pedido Confirmado");
                    return "pedidos/confirmacion";
                })
                .orElse("redirect:/pedidos");   // cae al historial
    }

    @GetMapping
    public String historial(Model m) {
        Long clienteId = getClienteId();
        if (clienteId == null) {
            return "redirect:/clientes/login";
        }

        m.addAttribute("pedidos", pedidoService.buscarPorClienteId(clienteId));
        m.addAttribute("title", "Mis Pedidos");
        return "pedidos/historial";
    }
}
