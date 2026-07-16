package edu.cit.capuras.studymate.config;

import edu.cit.capuras.studymate.auth.JwtUtil;
import edu.cit.capuras.studymate.auth.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Reads the "Authorization: Bearer <token>" header on every request. If the
 * token is present, valid, and not revoked, the authenticated user's ID is
 * attached to the request (attribute "authUserId") and to the Spring
 * Security context so downstream controllers/BR-003/BR-006/BR-008 ownership
 * checks can rely on it instead of trusting a client-supplied userId alone.
 *
 * Requests to /api/auth/** are allowed through without a token (register/login).
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (!tokenBlacklist.isRevoked(token)) {
                try {
                    Long userId = jwtUtil.validateAndGetUserId(token);
                    request.setAttribute("authUserId", userId);
                    request.setAttribute("authToken", token);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception ignored) {
                    // Invalid/expired token: leave the request unauthenticated.
                    // SecurityConfig will reject it with 401 for protected routes.
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
