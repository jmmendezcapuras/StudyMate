package edu.cit.capuras.studymate.dto;

public class AuthResponse {

    private Long id;
    private String username;

    public AuthResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
