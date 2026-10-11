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

import java.util.List;

@Controller
public class AccountController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public AccountController(
            ReservationRepository reservationRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
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
}
