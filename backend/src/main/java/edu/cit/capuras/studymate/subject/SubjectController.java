package edu.cit.capuras.studymate.subject;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // FR-011 / BR-003: the JWT identifies who is actually making the call.
    // If the caller's token belongs to a different account than the userId
    // they're asking to act on, the request is rejected before it ever
    // reaches the service/repository layer.
    private ResponseEntity<?> checkOwnership(HttpServletRequest request, Long userId) {
        Object authUserId = request.getAttribute("authUserId");
        if (authUserId == null || !authUserId.equals(userId)) {
            return ResponseEntity.status(403).body("Access denied: you may only access your own subjects");
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addSubject(HttpServletRequest request, @RequestParam Long userId, @RequestBody SubjectRequest req) {
        ResponseEntity<?> denied = checkOwnership(request, userId);
        if (denied != null) return denied;

        try {
            return ResponseEntity.ok(subjectService.addSubject(userId, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSubjects(HttpServletRequest request, @RequestParam Long userId) {
        ResponseEntity<?> denied = checkOwnership(request, userId);
        if (denied != null) return denied;

        try {
            return ResponseEntity.ok(subjectService.getSubjects(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
