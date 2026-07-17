package edu.cit.capuras.studymate.admin;

import edu.cit.capuras.studymate.auth.User;

/**
 * Deliberately separate from the User entity's default JSON serialization:
 * an admin needs to see who an account belongs to, but never the password
 * hash (already @JsonIgnore'd on User, but keeping admin responses on their
 * own DTO avoids ever accidentally exposing new sensitive fields later).
 */
public class UserSummaryResponse {

    private Long id;
    private String username;
    private String email;
    private String role;

    public UserSummaryResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
