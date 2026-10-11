package com.mycompany.cinema;

import com.mycompany.movie.Reservation;
import com.mycompany.movie.ReservationRepository;
import com.mycompany.movie.User;
import com.mycompany.movie.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class AccountController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AccountController(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            UserService userService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/conta")
    public String accountPage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Reservation> reservations =
                reservationRepository
                        .findByUserUsernameOrderByIdDesc(
                                userDetails.getUsername());

        User user = userRepository
                .findByUsername(userDetails.getUsername())
                .orElse(null);

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("fullName",
                user != null ? user.getFullName() : null);
        model.addAttribute("email",
                user != null ? user.getEmail() : null);
        model.addAttribute("reservations", reservations);

        return "account";
    }
    
    @PostMapping("/conta/editar")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        boolean updated = userService.updateProfile(
                userDetails.getUsername(),
                fullName,
                email
        );

        if (updated) {
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Os teus dados foram atualizados com sucesso."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Não foi possível atualizar os dados. Verifica o nome e o formato do e-mail, e confirma se o e-mail já está associado a outra conta."
            );
        }

        return "redirect:/conta";
    }
    
    
    @PostMapping("/conta/alterar-password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String result = userService.changePassword(
                userDetails.getUsername(),
                currentPassword,
                newPassword,
                confirmPassword
        );

        if (result == null) {
            redirectAttributes.addFlashAttribute(
                    "passwordSuccess",
                    "A palavra-passe foi alterada com sucesso."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "passwordError",
                    result
            );
        }

        return "redirect:/conta";
    }


}
