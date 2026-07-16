package edu.cit.capuras.studymate.session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:5173")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    // FR-011 / BR-006: only the authenticated owner of userId may act on
    // their sessions. Rejects the request with 403 before it reaches the
    // service layer if the caller's JWT identity doesn't match.
    private ResponseEntity<?> checkOwnership(HttpServletRequest request, Long userId) {
        Object authUserId = request.getAttribute("authUserId");
        if (authUserId == null || !authUserId.equals(userId)) {
            return ResponseEntity.status(403).body("Access denied: you may only access your own study sessions");
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addSession(HttpServletRequest request, @RequestParam Long userId, @RequestBody StudySessionRequest req) {
        ResponseEntity<?> denied = checkOwnership(request, userId);
        if (denied != null) return denied;

        try {
            return ResponseEntity.ok(studySessionService.addSession(userId, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSessions(HttpServletRequest request, @RequestParam Long userId) {
        ResponseEntity<?> denied = checkOwnership(request, userId);
        if (denied != null) return denied;

        try {
            return ResponseEntity.ok(studySessionService.getSessions(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(HttpServletRequest request, @PathVariable Long id, @RequestParam Long userId) {
        ResponseEntity<?> denied = checkOwnership(request, userId);
        if (denied != null) return denied;

        try {
            studySessionService.deleteSession(userId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
