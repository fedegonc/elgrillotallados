package com.elgrillo.demo.cliente.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Entidad Cliente que representa a un usuario registrado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private String apellido;
    
    private String email;
    
    private String telefono;
    
    private String direccion;
    
    private String ciudad;
    
    private String codigoPostal;
    
    private String departamento;
    
    // Password encriptado para Spring Security
    private String password;
    
    // Roles del usuario separados por comas (ROLE_USER, ROLE_ADMIN, etc.)
    private String roles;
    
    private LocalDateTime fechaRegistro;
    
    private LocalDateTime ultimoAcceso;
    
    private Boolean activo;
    
    /**
     * Retorna la lista de roles del usuario
     */
    public List<String> getRoleList() {
        if (this.roles == null || this.roles.isEmpty()) {
            return List.of("ROLE_USER"); // Por defecto, todos tienen al menos ROLE_USER
        }
        return Arrays.asList(this.roles.split(","));
    }
    
    /**
     * Asigna un rol al usuario
     */
    public void addRole(String role) {
        if (this.roles == null || this.roles.isEmpty()) {
            this.roles = role;
        } else if (!this.roles.contains(role)) {
            this.roles = this.roles + "," + role;
        }
    }
}
