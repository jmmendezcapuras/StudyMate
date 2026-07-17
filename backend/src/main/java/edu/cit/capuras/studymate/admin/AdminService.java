package edu.cit.capuras.studymate.admin;

import edu.cit.capuras.studymate.auth.User;
import edu.cit.capuras.studymate.auth.UserRepository;
import edu.cit.capuras.studymate.session.StudySessionRepository;
import edu.cit.capuras.studymate.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserSummaryResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Realistic admin onboarding: only an already-authenticated admin can
     * create another admin account (enforced by ROLE_ADMIN on
     * /api/admin/** in SecurityConfig — this method is unreachable to a
     * student's token). This is how the seeded bootstrap admin (AdminSeeder)
     * is meant to be used in practice: log in once with the seeded
     * credentials, then create a real admin account (e.g. for the
     * instructor) through this endpoint instead of sharing the seed
     * password long-term.
     */
    @Transactional
    public UserSummaryResponse createAdmin(CreateAdminRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (req.getEmail() == null || !EMAIL_PATTERN.matcher(req.getEmail().trim()).matches()) {
            throw new RuntimeException("Please enter a valid email address");
        }
        if (req.getPassword() == null || req.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already registered");
        }
        if (userRepository.existsByEmailIgnoreCase(req.getEmail().trim())) {
            throw new RuntimeException("Email already registered");
        }

        User admin = new User();
        admin.setUsername(req.getUsername());
        admin.setEmail(req.getEmail().trim());
        admin.setPassword(passwordEncoder.encode(req.getPassword()));
        admin.setRole("ADMIN");

        userRepository.save(admin);
        return new UserSummaryResponse(admin);
    }

    /**
     * Deletes a user account along with everything that belongs to it.
     * Order matters for referential integrity: sessions (which reference
     * both user and subject) go first, then subjects, then the user.
     */
    @Transactional
    public void deleteUser(Long adminUserId, Long targetUserId) {
        if (adminUserId.equals(targetUserId)) {
            throw new RuntimeException("You cannot delete your own admin account while logged in as it");
        }

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        studySessionRepository.deleteByUser(target);
        subjectRepository.deleteByUser(target);
        userRepository.delete(target);
    }
}
