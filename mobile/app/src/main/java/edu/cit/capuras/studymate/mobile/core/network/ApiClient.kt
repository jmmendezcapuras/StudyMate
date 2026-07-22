package edu.cit.capuras.studymate.mobile.core.network

import edu.cit.capuras.studymate.mobile.feature.admin.AdminApiService
import edu.cit.capuras.studymate.mobile.feature.auth.AuthApiService
import edu.cit.capuras.studymate.mobile.feature.session.SessionApiService
import edu.cit.capuras.studymate.mobile.feature.subject.SubjectApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Attaches the current session's JWT to every request so the backend's
    // JwtAuthFilter can authenticate the caller (NFR-006 / BR-008).
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val original = chain.request()
        val token = TokenProvider.token
        val request = if (token != null) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
    val subjectApi: SubjectApiService = retrofit.create(SubjectApiService::class.java)
    val sessionApi: SessionApiService = retrofit.create(SessionApiService::class.java)
    val adminApi: AdminApiService = retrofit.create(AdminApiService::class.java)
}
