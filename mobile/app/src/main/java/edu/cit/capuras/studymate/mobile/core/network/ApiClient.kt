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

    // Render's free tier spins the backend down after inactivity; the first
    // request after a cold start can take 30-50+ seconds while it wakes
    // back up. 10.0.2.2 was the emulator-only alias for a locally running
    // backend — replaced with the actual deployed backend URL.
    private const val BASE_URL = "https://studymate-552b.onrender.com/api/"

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
        // Long enough to survive a Render cold start rather than fail with
        // a timeout on the very first request after idling.
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
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
