package edu.cit.capuras.studymate.mobile.feature.auth

data class RegisterRequest(
    val username: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val id: Long,
    val username: String,
    val token: String
)
