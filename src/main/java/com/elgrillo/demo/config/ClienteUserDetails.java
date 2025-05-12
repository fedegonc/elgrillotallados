package com.elgrillo.demo.config;

import com.elgrillo.demo.cliente.model.Cliente;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ClienteUserDetails implements UserDetails {

    @Getter
    private final Cliente cliente;
    private final List<GrantedAuthority> authorities;

    public ClienteUserDetails(Cliente cliente) {
        this.cliente = cliente;
        
        // Por defecto, todos los clientes tienen el rol USER
        // Si el email contiene "admin", asignarle también el rol ADMIN (solo para pruebas)
        if (cliente.getEmail() != null && cliente.getEmail().contains("admin")) {
            this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        } else {
            this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return cliente.getPassword();
    }

    @Override
    public String getUsername() {
        return cliente.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
