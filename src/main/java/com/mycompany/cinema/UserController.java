package com.mycompany.cinema;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || username == null || username.isBlank()
                || password == null || password.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {

            model.addAttribute(
                    "error",
                    "Preenche todos os campos."
            );
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute(
                    "error",
                    "As palavras-passe não coincidem."
            );
            return "register";
        }

        if (password.length() < 8) {
            model.addAttribute(
                    "error",
                    "A palavra-passe deve ter pelo menos 8 caracteres."
            );
            return "register";
        }

        boolean registered = userService.register(
                fullName,
                email,
                username,
                password
        );

        if (!registered) {
            model.addAttribute(
                    "error",
                    "Não foi possível criar a conta. Verifica os dados introduzidos, o formato do e-mail e se o nome de utilizador ou e-mail já estão registados."
            );
            return "register";
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Conta criada com sucesso. Já podes iniciar sessão."
        );

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/funcionario/login")
    public String funcionarioLoginPage() {
        return "login-funcionario";
    }
}

