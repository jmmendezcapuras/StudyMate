package edu.cit.capuras.studymate.subject;

import edu.cit.capuras.studymate.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUser(User user);

    boolean existsByUserAndNameIgnoreCase(User user, String name);

    // Enforces ownership on delete, the same pattern already used for
    // study sessions (findByIdAndUser).
    Optional<Subject> findByIdAndUser(Long id, User user);

    // Used by admin user deletion to clear a user's subjects before
    // removing the account itself (referential integrity).
    void deleteByUser(User user);
}
