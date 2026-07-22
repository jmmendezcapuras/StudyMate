package edu.cit.capuras.studymate.mobile.feature.admin

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Talks to AdminController's admin-users REST endpoints. These are already
 * restricted server-side to ROLE_ADMIN, so a non-admin JWT will get a 403
 * regardless of what this client does — the role check in AdminViewModel
 * / navigation is just so the app doesn't dead-end a student here.
 */
interface AdminApiService {

    @GET("admin/users")
    suspend fun getAllUsers(): Response<List<UserSummaryResponse>>

    @POST("admin/users")
    suspend fun createAdmin(@Body request: CreateAdminRequest): Response<UserSummaryResponse>

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long): Response<Unit>
}
