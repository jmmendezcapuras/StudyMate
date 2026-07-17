package edu.cit.capuras.studymate.config;

import edu.cit.capuras.studymate.auth.JwtUtil;
import edu.cit.capuras.studymate.auth.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads the "Authorization: Bearer <token>" header on every request. If the
 * token is present, valid, and not revoked, the authenticated user's ID and
 * role are attached to the request ("authUserId", "authRole") and to the
 * Spring Security context (as a ROLE_<role> authority) so downstream
 * ownership checks (BR-003/BR-006/BR-008) and admin-only routes can rely on
 * it instead of trusting client-supplied values alone.
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
                    String role = jwtUtil.getRole(token);
                    if (role == null) {
                        role = "STUDENT"; // tokens issued before roles existed
                    }

                    request.setAttribute("authUserId", userId);
                    request.setAttribute("authRole", role);
                    request.setAttribute("authToken", token);

                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, authorities);
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
