package edu.cit.capuras.studymate.controller;

import edu.cit.capuras.studymate.dto.SubjectRequest;
import edu.cit.capuras.studymate.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // NOTE: userId is passed as a request param for now since there is no JWT
    // filter wired up yet. Replace with the authenticated principal once
    // token-based auth is implemented.

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
