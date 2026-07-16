package edu.cit.capuras.studymate.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Handles creation and validation of JWT access tokens.
 * Supports NFR-006 (JWT-based authentication) and BR-008 (all protected
 * requests must carry a valid JWT token).
 */
@Component
public class JwtUtil {

    // Fallback secret is fine for a student/academic deployment; production
    // systems should always inject this via an environment variable.
    @Value("${studymate.jwt.secret:studymate-super-secret-key-for-jwt-signing-please-change-me}")
    private String secret;

    @Value("${studymate.jwt.expiration-ms:86400000}") // 24 hours
    private long expirationMs;

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    /**
     * Validates the token signature/expiry and returns the embedded user ID.
     * Throws JwtException (or a subclass) if the token is invalid/expired.
     */
    public Long validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("username", String.class);
    }
}
