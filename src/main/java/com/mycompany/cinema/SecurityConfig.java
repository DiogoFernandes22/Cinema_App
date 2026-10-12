
package com.mycompany.cinema;

import com.mycompany.movie.FuncionarioUserDetailsService;
import com.mycompany.movie.User;
import com.mycompany.movie.UserRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // API administrativa: autenticação dos funcionários por HTTP Basic.
    @Bean
    @Order(1)
    public SecurityFilterChain adminApiSecurityFilterChain(
            HttpSecurity http,
            FuncionarioUserDetailsService funcionarioService,
            PasswordEncoder passwordEncoder) throws Exception {

        DaoAuthenticationProvider funcionarioProvider =
                new DaoAuthenticationProvider(funcionarioService);

        funcionarioProvider.setPasswordEncoder(passwordEncoder);

        http
            .securityMatcher("/api/admin/**")
            .authenticationProvider(funcionarioProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/funcionarios/**")
                    .hasRole("ADMIN")
                .anyRequest()
                    .hasAnyRole("ADMIN", "STAFF")
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Login de funcionários e página de gestão.
    @Bean
    @Order(2)
    public SecurityFilterChain funcionarioSecurityFilterChain(
            HttpSecurity http,
            FuncionarioUserDetailsService funcionarioService,
            PasswordEncoder passwordEncoder) throws Exception {

        DaoAuthenticationProvider funcionarioProvider =
                new DaoAuthenticationProvider(funcionarioService);

        funcionarioProvider.setPasswordEncoder(passwordEncoder);

        http
            .securityMatcher(
                "/funcionario/**",
                "/admin/funcionarios",
                "/admin/funcionarios/**"
            )
            .authenticationProvider(funcionarioProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/funcionario/login").permitAll()
                .requestMatchers(
                    "/admin/funcionarios",
                    "/admin/funcionarios/**"
                ).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/funcionario/login")
                .loginProcessingUrl("/funcionario/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/funcionario/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/funcionario/logout")
                .logoutSuccessUrl("/funcionario/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // Site público e login dos clientes.
    @Bean
    @Order(3)
    public SecurityFilterChain websiteSecurityFilterChain(
            HttpSecurity http,
            @Qualifier("userDetailsService")
            UserDetailsService clientUserDetailsService,
            PasswordEncoder passwordEncoder) throws Exception {

        DaoAuthenticationProvider clientProvider =
                new DaoAuthenticationProvider(clientUserDetailsService);

        clientProvider.setPasswordEncoder(passwordEncoder);

        http
            .authenticationProvider(clientProvider)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // Autenticação dos clientes.
    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() ->
                        new UsernameNotFoundException(
                            "Utilizador não encontrado."
                        )
                    );

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }
}
