package com.mycompany.movie;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioUserDetailsService(
            FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Funcionario funcionario = funcionarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Funcionário não encontrado."));

        if (!funcionario.isAtivo()) {
            throw new UsernameNotFoundException(
                    "Esta conta de funcionário está inativa.");
        }

        String role = funcionario.getRole();

        if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
            throw new UsernameNotFoundException(
                    "Função de funcionário inválida.");
        }

        return new org.springframework.security.core.userdetails.User(
                funcionario.getUsername(),
                funcionario.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}