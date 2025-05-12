package com.elgrillo.demo.cliente.service;

import com.elgrillo.demo.cliente.model.Cliente;
import com.elgrillo.demo.cliente.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de clientes.
 * Contiene la lógica de negocio relacionada con los clientes.
 */
@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Obtiene todos los clientes
     */
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }
    
    /**
     * Busca un cliente por su ID
     */
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Busca un cliente por su email
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    /**
     * Registra un nuevo cliente
     */
    public Cliente registrar(Cliente cliente) {
        // Marcar como activo y establecer la fecha de registro
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setUltimoAcceso(LocalDateTime.now());
        
        // Aquí debería implementarse el hash de la contraseña
        // En una implementación real usaríamos BCrypt o similar
        // cliente.setPassword(passwordEncoder.encode(password));
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Actualiza los datos de un cliente existente
     */
    public Cliente actualizar(Cliente cliente) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(cliente.getId());
        if (clienteOpt.isPresent()) {
            cliente.setUltimoAcceso(LocalDateTime.now());
            // No actualizamos password ni fechaRegistro
            return clienteRepository.save(cliente);
        }
        return null;
    }
    
    /**
     * Registra acceso del cliente (actualiza último acceso)
     */
    public Cliente registrarAcceso(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setUltimoAcceso(LocalDateTime.now());
            return clienteRepository.save(cliente);
        }
        return null;
    }
    
    /**
     * Desactiva un cliente (baja lógica)
     */
    public Cliente desactivar(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setActivo(false);
            return clienteRepository.save(cliente);
        }
        return null;
    }
    
    // Method to check if an email already exists
    public boolean emailExists(String email) {
        return clienteRepository.existsByEmail(email);
    }
}
