package com.elgrillo.demo.cliente.repository;

import com.elgrillo.demo.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su dirección de correo electrónico
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Verifica si existe un cliente con el email proporcionado
     */
    boolean existsByEmail(String email);
}
