package edu.cit.capuras.studymate.repository;

import edu.cit.capuras.studymate.model.StudySession;
import edu.cit.capuras.studymate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUser(User user);

    Optional<StudySession> findByIdAndUser(Long id, User user);
}
