package edu.cit.capuras.studymate.admin;

import edu.cit.capuras.studymate.auth.User;
import edu.cit.capuras.studymate.auth.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds exactly one admin account on application startup, if it doesn't
 * already exist. There is deliberately no public "register as admin"
 * endpoint — admin accounts are provisioned this way, not through
 * self-registration, so a student can never grant themselves admin access.
 * Credentials come from application.properties (see studymate.admin.*),
 * which is gitignored, so no real credentials are committed to source
 * control.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${studymate.admin.username:admin}")
    private String adminUsername;

    @Value("${studymate.admin.email:admin@studymate.local}")
    private String adminEmail;

    @Value("${studymate.admin.password:ChangeMe123!}")
    private String adminPassword;

    public AdminSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");

        userRepository.save(admin);
    }
}
