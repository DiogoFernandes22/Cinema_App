
package com.mycompany.cinema;

import com.mycompany.movie.User;
import com.mycompany.movie.UserRepository;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(
            String fullName,
            String email,
            String username,
            String password) {

        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return false;
        }

        String normalizedFullName = fullName.trim();
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedUsername = username.trim();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            return false;
        }

        if (userRepository.existsByEmail(normalizedEmail)) {
            return false;
        }

        User user = new User(
                normalizedUsername,
                passwordEncoder.encode(password),
                normalizedFullName,
                normalizedEmail
        );

        userRepository.save(user);

        return true;
    }
}
