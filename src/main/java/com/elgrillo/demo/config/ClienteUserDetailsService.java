package com.elgrillo.demo.config;

import com.elgrillo.demo.cliente.model.Cliente;
import com.elgrillo.demo.cliente.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(username);
        if (clienteOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return new ClienteUserDetails(clienteOpt.get());
    }
    
    /**
     * Registra un nuevo cliente con el password ya codificado
     * @param cliente Cliente con el password ya codificado
     * @return Cliente recién registrado
     */
    public Cliente registrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
