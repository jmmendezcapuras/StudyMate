package edu.cit.capuras.studymate.mobile.feature.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Talks to the StudyMate Spring Boot backend authentication endpoints.
 */
interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
