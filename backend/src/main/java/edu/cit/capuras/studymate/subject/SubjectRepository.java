package edu.cit.capuras.studymate.subject;

import edu.cit.capuras.studymate.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUser(User user);

    boolean existsByUserAndNameIgnoreCase(User user, String name);
}
