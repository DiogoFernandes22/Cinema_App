
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
    
    public boolean updateProfile(
            String username,
            String fullName,
            String email) {

        if (username == null || username.isBlank()
                || fullName == null || fullName.isBlank()
                || email == null || email.isBlank()) {
            return false;
        }

        String normalizedFullName = fullName.trim();
        String normalizedEmail = email.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            return false;
        }

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return false;
        }

        if (!normalizedEmail.equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(normalizedEmail)) {
            return false;
        }

        user.setFullName(normalizedFullName);
        user.setEmail(normalizedEmail);

        userRepository.save(user);

        return true;
    }
    
    
    public String changePassword(
            String username,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (currentPassword == null || currentPassword.isBlank()
                || newPassword == null || newPassword.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {
            return "Preenche todos os campos da palavra-passe.";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "As novas palavras-passe não coincidem.";
        }

        if (newPassword.length() < 8) {
            return "A nova palavra-passe deve ter pelo menos 8 caracteres.";
        }

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return "Não foi possível encontrar a conta.";
        }

        if (!passwordEncoder.matches(
                currentPassword, user.getPassword())) {
            return "A palavra-passe atual está incorreta.";
        }

        if (passwordEncoder.matches(
                newPassword, user.getPassword())) {
            return "A nova palavra-passe deve ser diferente da atual.";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return null;
    }


}
