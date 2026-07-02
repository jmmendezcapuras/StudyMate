package edu.cit.capuras.studymate.mobile.network

import edu.cit.capuras.studymate.mobile.network.dto.AuthResponse
import edu.cit.capuras.studymate.mobile.network.dto.LoginRequest
import edu.cit.capuras.studymate.mobile.network.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Talks to the StudyMate Spring Boot backend's auth endpoints
 * (edu.cit.capuras.studymate.controller.AuthController).
 */
interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
