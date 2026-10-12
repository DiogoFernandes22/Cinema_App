
package com.mycompany.movie;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByUsername(String username);
}
