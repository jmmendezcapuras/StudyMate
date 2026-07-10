package edu.cit.capuras.studymate.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:5173")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @PostMapping
    public ResponseEntity<?> addSession(@RequestParam Long userId, @RequestBody StudySessionRequest req) {
        try {
            return ResponseEntity.ok(studySessionService.addSession(userId, req));
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
    public ResponseEntity<?> deleteSession(@PathVariable Long id, @RequestParam Long userId) {
        try {
            studySessionService.deleteSession(userId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
