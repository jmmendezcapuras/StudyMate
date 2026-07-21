package edu.cit.capuras.studymate.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * All routes here are already restricted to ROLE_ADMIN by SecurityConfig
 * (.requestMatchers("/api/admin/**").hasRole("ADMIN")) — a student's JWT
 * simply cannot reach this controller, regardless of what userId they pass.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "https://studymateweb.onrender.com"})
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminRequest req) {
        try {
            return ResponseEntity.ok(adminService.createAdmin(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @PathVariable Long id) {
        Long adminUserId = (Long) request.getAttribute("authUserId");
        try {
            adminService.deleteUser(adminUserId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
