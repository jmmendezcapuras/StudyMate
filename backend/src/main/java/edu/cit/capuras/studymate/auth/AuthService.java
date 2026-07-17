package edu.cit.capuras.studymate.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(req.getEmail().trim()).matches()) {
            throw new RuntimeException("Please enter a valid email address");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        // BR-001: duplicate usernames are not allowed.
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already registered");
        }
        // Same rule extended to email: one account per email address.
        if (userRepository.existsByEmailIgnoreCase(req.getEmail().trim())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), token);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), token);
    }

    /**
     * FR-003 / BR-008: invalidate the caller's JWT so it can no longer be
     * used to access protected endpoints, even though it hasn't expired yet.
     */
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("No active session token provided");
        }
        tokenBlacklist.revoke(token);
    }
}
