package edu.cit.capuras.studymate.mobile.feature.session

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SessionApiService {

    @GET("sessions")
    suspend fun getSessions(@Query("userId") userId: Long): Response<List<SessionResponse>>

    @POST("sessions")
    suspend fun addSession(
        @Query("userId") userId: Long,
        @Body request: SessionRequest
    ): Response<SessionResponse>

    @DELETE("sessions/{id}")
    suspend fun deleteSession(
        @Path("id") id: Long,
        @Query("userId") userId: Long
    ): Response<Unit>
}
