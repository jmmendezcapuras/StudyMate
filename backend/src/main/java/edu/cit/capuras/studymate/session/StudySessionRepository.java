package edu.cit.capuras.studymate.session;

import edu.cit.capuras.studymate.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserOrderBySessionDateDesc(User user);

    Optional<StudySession> findByIdAndUser(Long id, User user);
}
