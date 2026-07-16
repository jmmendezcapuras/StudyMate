package edu.cit.capuras.studymate.mobile.feature.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // FR-003: revokes the current JWT server-side. The token itself is
    // attached automatically by ApiClient's auth interceptor.
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
