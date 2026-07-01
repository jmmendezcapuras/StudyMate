package edu.cit.capuras.studymate.controller;

import edu.cit.capuras.studymate.dto.StudySessionRequest;
import edu.cit.capuras.studymate.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:5173")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    // NOTE: userId is passed as a request param for now since there is no JWT
    // filter wired up yet. Replace with the authenticated principal once
    // token-based auth is implemented.

    @PostMapping
    public ResponseEntity<?> logSession(@RequestParam Long userId, @RequestBody StudySessionRequest req) {
        try {
            return ResponseEntity.ok(studySessionService.logSession(userId, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSessions(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(studySessionService.getSessions(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@RequestParam Long userId, @PathVariable Long id) {
        try {
            studySessionService.deleteSession(userId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
