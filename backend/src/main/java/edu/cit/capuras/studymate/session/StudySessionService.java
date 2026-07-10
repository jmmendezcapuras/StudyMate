package edu.cit.capuras.studymate.session;

import edu.cit.capuras.studymate.auth.User;
import edu.cit.capuras.studymate.auth.UserRepository;
import edu.cit.capuras.studymate.subject.Subject;
import edu.cit.capuras.studymate.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Session slice service. Depends on both the auth slice (User) and the
 * subject slice (Subject) — mirroring the one intentional cross-slice
 * dependency pattern already used by the subject slice (subject -> auth).
 * Sessions depend on subject and auth; neither depends back on session.
 */
@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public StudySession addSession(Long userId, StudySessionRequest req) {
        User user = getUser(userId);

        // FR-008 / BR-005: subject, duration and date are required before a
        // session can be saved. Notes remain optional.
        if (req.getSubjectId() == null) {
            throw new RuntimeException("Subject is required");
        }
        if (req.getDurationMinutes() == null || req.getDurationMinutes() <= 0) {
            throw new RuntimeException("Duration must be a positive number of minutes");
        }
        if (req.getSessionDate() == null) {
            throw new RuntimeException("Date is required");
        }

        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // BR-003: a session can only be logged against a subject owned by
        // the same authenticated user.
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("That subject does not belong to this user");
        }

        StudySession session = new StudySession();
        session.setUser(user);
        session.setSubject(subject);
        session.setDurationMinutes(req.getDurationMinutes());
        session.setSessionDate(req.getSessionDate());
        session.setNotes(req.getNotes());

        return studySessionRepository.save(session);
    }

    public List<StudySession> getSessions(Long userId) {
        User user = getUser(userId);
        return studySessionRepository.findByUserOrderBySessionDateDesc(user);
    }

    public void deleteSession(Long userId, Long sessionId) {
        User user = getUser(userId);

        // BR-006: users may only delete their own study sessions.
        StudySession session = studySessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new RuntimeException("Study session not found"));

        studySessionRepository.delete(session);
    }
}
