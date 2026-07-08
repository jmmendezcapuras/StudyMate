package edu.cit.capuras.studymate.mobile.feature.auth

/**
 * Mirrors edu.cit.capuras.studymate.dto.RegisterRequest on the backend.
 */
data class RegisterRequest(
    val username: String,
    val password: String
)

/**
 * Mirrors edu.cit.capuras.studymate.dto.LoginRequest on the backend.
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Mirrors edu.cit.capuras.studymate.dto.AuthResponse on the backend.
 */
data class AuthResponse(
    val id: Long,
    val username: String
)
