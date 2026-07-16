package edu.cit.capuras.studymate.auth;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks JWT tokens that have been explicitly logged out (FR-003 / BR-008).
 * Since JWTs are stateless by design, revocation needs an explicit registry;
 * an in-memory set is sufficient for this project's scope (single backend
 * instance, tokens expire automatically after 24h regardless).
 */
@Component
public class TokenBlacklist {

    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    public void revoke(String token) {
        revokedTokens.add(token);
    }

    public boolean isRevoked(String token) {
        return revokedTokens.contains(token);
    }
}
