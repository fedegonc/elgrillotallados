package com.elgrillo.demo.cliente.controller;

import com.elgrillo.demo.cliente.model.Cliente;
import com.elgrillo.demo.cliente.service.ClienteService;
import com.elgrillo.demo.config.ClienteUserDetailsService;
import com.elgrillo.demo.producto.model.Producto;
import com.elgrillo.demo.producto.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final PasswordEncoder passwordEncoder;
    private final ClienteUserDetailsService userDetailsService;
    
    @Autowired
    private ProductoService productoService;

    public ClienteController(ClienteService clienteService,
                             PasswordEncoder passwordEncoder,
                             ClienteUserDetailsService userDetailsService) {
        this.clienteService   = clienteService;
        this.passwordEncoder  = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /* ---------------- 1. REGISTRO -------------------------------------- */

    @GetMapping("/registro")
    public String formRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("title", "Registrarse");
        return "clientes/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Cliente cliente, Model model) {
        if (clienteService.emailExists(cliente.getEmail())) {
            model.addAttribute("error", "El email ya está registrado");
            model.addAttribute("cliente", cliente);
            return "clientes/registro";
        } else {
            // Encriptar password antes de guardar
            cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
            
            // Establecer usuario como activo
            cliente.setActivo(true);
            
            Cliente clienteGuardado = userDetailsService.registrarCliente(cliente);
            clienteService.registrarAcceso(clienteGuardado.getId());
            
            // Autenticar al usuario automáticamente después del registro
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                cliente.getEmail(),
                null, // No incluimos el password encriptado en el token
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Redirigir directamente al catálogo de productos
            return "redirect:/productos";
        }
    }

    /* ---------------- 2. LOGIN (solo vista) ---------------------------- */

    @GetMapping("/login")
    public String formLogin(Model model,
                            @RequestParam(required = false) Boolean error,
                            @RequestParam(required = false) Boolean registroExitoso) {

        model.addAttribute("title", "Iniciar Sesión");
        if (Boolean.TRUE.equals(error))
            model.addAttribute("error", "Email o contraseña incorrectos");

        if (Boolean.TRUE.equals(registroExitoso))
            model.addAttribute("mensaje", "Registro exitoso. Ya puedes iniciar sesión.");
            
        // Obtener todos los usuarios para mostrar en la tabla de desarrollo
        List<Cliente> usuarios = clienteService.obtenerTodos();
        System.out.println("===== INFORMACIÓN DE DEPURACIÓN =====");
        System.out.println("Número de usuarios encontrados: " + usuarios.size());
        
        // Mostrar información detallada de cada usuario para depuración
        for (Cliente usuario : usuarios) {
            System.out.println("Usuario ID: " + usuario.getId() + 
                             ", Nombre: " + usuario.getNombre() + 
                             ", Email: " + usuario.getEmail() + 
                             ", Activo: " + usuario.getActivo());
        }
        
        // Asegurarse de que la lista no sea nula antes de pasarla al modelo
        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }
        
        model.addAttribute("usuarios", usuarios);
        System.out.println("Usuarios agregados al modelo: " + usuarios.size());
        
        return "clientes/login";
    }

    /* ---------------- 3. PERFIL (GET & POST) --------------------------- */

    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        Optional<Cliente> cli = clienteActual();
        cli.ifPresent(c -> {
            model.addAttribute("cliente", c);
            model.addAttribute("title", "Mi Perfil");
        });
        return "clientes/perfil";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute Cliente dto) {
        clienteActual().ifPresent(c -> {
            c.setNombre(dto.getNombre());
            c.setDireccion(dto.getDireccion());
            c.setTelefono(dto.getTelefono());
            clienteService.actualizar(c);
        });
        return "redirect:/clientes/perfil?actualizado=true";
    }

    /* ---------------- AUX ------------------------------------------------ */

    private Optional<Cliente> clienteActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return clienteService.buscarPorEmail(auth.getName());
    }
    
    /**
     * Página principal de clientes
     */
    @GetMapping("") // Este mapeo se combina con "/clientes" del RequestMapping de la clase
    public String index(Model model) {
        model.addAttribute("title", "Tallado Criollo - Área de Clientes");
        
        // Obtener productos destacados usando el servicio
        List<Producto> productosDestacados = productoService.obtenerDestacados();
        model.addAttribute("productos", productosDestacados);
        
        return "clientes/index"; // Asegurar que apunte a una vista en el directorio correcto
    }
    
    /**
     * Panel principal para usuarios autenticados
     */
    @GetMapping("/home")
    public String home(Model model) {
        // Agregar título a la página
        model.addAttribute("title", "Panel Principal");
        
        return "home";
    }
    
    /**
     * Página de bienvenida después del registro
     */
    @GetMapping("/bienvenida")
    public String mostrarBienvenida(Model model) {
        // Obtener el usuario autenticado del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(email);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            model.addAttribute("title", "Bienvenido");
        }
        
        return "clientes/bienvenida";
    }
}
