package edu.cit.capuras.studymate.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<?> addSubject(@RequestParam Long userId, @RequestBody SubjectRequest req) {
        try {
            return ResponseEntity.ok(subjectService.addSubject(userId, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSubjects(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(subjectService.getSubjects(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
