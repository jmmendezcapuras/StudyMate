package edu.cit.capuras.studymate.mobile.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 10.0.2.2 is the Android emulator's alias for your host machine's
    // localhost, so this hits the Spring Boot backend running locally on
    // port 8080 (see backend/README / application.properties).
    //
    // - Running on a physical device on the same Wi-Fi as your backend?
    //   Replace this with your computer's LAN IP, e.g. "http://192.168.1.5:8080/api/"
    // - Deployed the backend somewhere (Render, Railway, etc.)?
    //   Replace this with that public HTTPS URL instead.
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
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
}
