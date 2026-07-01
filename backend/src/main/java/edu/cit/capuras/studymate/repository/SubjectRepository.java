package edu.cit.capuras.studymate.repository;

import edu.cit.capuras.studymate.model.Subject;
import edu.cit.capuras.studymate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUser(User user);

    boolean existsByUserAndNameIgnoreCase(User user, String name);
}
