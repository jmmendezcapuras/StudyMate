package edu.cit.capuras.studymate.service;

import edu.cit.capuras.studymate.dto.StudySessionRequest;
import edu.cit.capuras.studymate.model.StudySession;
import edu.cit.capuras.studymate.model.Subject;
import edu.cit.capuras.studymate.model.User;
import edu.cit.capuras.studymate.repository.StudySessionRepository;
import edu.cit.capuras.studymate.repository.SubjectRepository;
import edu.cit.capuras.studymate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public StudySession logSession(Long userId, StudySessionRequest req) {
        User user = getUser(userId);

        if (req.getSubjectId() == null || req.getDurationMinutes() == null || req.getSessionDate() == null) {
            throw new RuntimeException("Subject, duration, and date are required");
        }

        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(userId)) {
            throw new RuntimeException("Subject does not belong to this user");
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
        return studySessionRepository.findByUser(user);
    }

    public void deleteSession(Long userId, Long sessionId) {
        User user = getUser(userId);
        StudySession session = studySessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new RuntimeException("Study session not found"));

        studySessionRepository.delete(session);
    }
}
