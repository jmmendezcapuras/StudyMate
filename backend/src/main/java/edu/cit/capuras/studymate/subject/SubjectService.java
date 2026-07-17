package edu.cit.capuras.studymate.subject;

import edu.cit.capuras.studymate.auth.User;
import edu.cit.capuras.studymate.auth.UserRepository;
import edu.cit.capuras.studymate.session.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Subject addSubject(Long userId, SubjectRequest req) {
        User user = getUser(userId);

        if (req.getName() == null || req.getName().isBlank()) {
            throw new RuntimeException("Subject name is required");
        }

        // App-level check first (fast path, friendly message for the common case).
        if (subjectRepository.existsByUserAndNameIgnoreCase(user, req.getName())) {
            throw new RuntimeException("Subject already exists");
        }

        Subject subject = new Subject();
        subject.setUser(user);
        subject.setName(req.getName());

        try {
            // The DB-level unique constraint on (user_id, name) is the real
            // guarantee: it catches the race where two concurrent requests
            // (e.g. two browser tabs/sessions) both pass the check above
            // before either has committed. Without this, both inserts would
            // otherwise succeed, producing duplicate subjects.
            return subjectRepository.saveAndFlush(subject);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Subject already exists");
        }
    }

    public List<Subject> getSubjects(Long userId) {
        User user = getUser(userId);
        return subjectRepository.findByUser(user);
    }

    @Transactional
    public void deleteSubject(Long userId, Long subjectId) {
        User user = getUser(userId);

        // Ownership check: a user may only delete their own subject,
        // mirroring the same findByIdAndUser pattern already used for
        // study sessions (BR-003).
        Subject subject = subjectRepository.findByIdAndUser(subjectId, user)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Preserve referential integrity: a study session always belongs to
        // exactly one subject, so deleting a subject that still has logged
        // sessions would either orphan them or violate the FK constraint.
        // Ask the user to remove those sessions first instead of silently
        // cascading the delete.
        if (studySessionRepository.existsBySubject(subject)) {
            throw new RuntimeException(
                "Cannot delete this subject: it still has study sessions logged against it. Delete those sessions first.");
        }

        subjectRepository.delete(subject);
    }
}
