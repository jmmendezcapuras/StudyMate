package edu.cit.capuras.studymate.session;

import edu.cit.capuras.studymate.auth.User;
import edu.cit.capuras.studymate.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserOrderBySessionDateDesc(User user);

    Optional<StudySession> findByIdAndUser(Long id, User user);

    // Used to block deleting a subject that still has sessions logged
    // against it, preserving referential integrity (a session must always
    // belong to exactly one subject).
    boolean existsBySubject(Subject subject);

    // Used by admin user deletion to clear a user's sessions before their
    // subjects and account (referential integrity, deletion order).
    void deleteByUser(User user);
}
