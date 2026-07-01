package edu.cit.capuras.studymate.service;

import edu.cit.capuras.studymate.dto.SubjectRequest;
import edu.cit.capuras.studymate.model.Subject;
import edu.cit.capuras.studymate.model.User;
import edu.cit.capuras.studymate.repository.SubjectRepository;
import edu.cit.capuras.studymate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Subject addSubject(Long userId, SubjectRequest req) {
        User user = getUser(userId);

        if (req.getName() == null || req.getName().isBlank()) {
            throw new RuntimeException("Subject name is required");
        }

        if (subjectRepository.existsByUserAndNameIgnoreCase(user, req.getName())) {
            throw new RuntimeException("Subject already exists");
        }

        Subject subject = new Subject();
        subject.setUser(user);
        subject.setName(req.getName());

        return subjectRepository.save(subject);
    }

    public List<Subject> getSubjects(Long userId) {
        User user = getUser(userId);
        return subjectRepository.findByUser(user);
    }
}
