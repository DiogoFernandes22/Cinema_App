
package com.mycompany.cinema;

import com.mycompany.movie.Funcionario;
import com.mycompany.movie.FuncionarioRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/funcionarios")
public class AdminFuncionarioController {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminFuncionarioController(
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listarFuncionarios(Model model) {
        List<Funcionario> funcionarios =
                funcionarioRepository.findAll();

        model.addAttribute("funcionarios", funcionarios);

        return "admin-funcionarios";
    }
}
