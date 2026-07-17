package edu.cit.capuras.studymate.mobile.feature.subject

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SubjectApiService {

    @GET("subjects")
    suspend fun getSubjects(@Query("userId") userId: Long): Response<List<SubjectResponse>>

    @POST("subjects")
    suspend fun addSubject(
        @Query("userId") userId: Long,
        @Body request: SubjectRequest
    ): Response<SubjectResponse>

    @DELETE("subjects/{id}")
    suspend fun deleteSubject(
        @Path("id") subjectId: Long,
        @Query("userId") userId: Long
    ): Response<Unit>
}