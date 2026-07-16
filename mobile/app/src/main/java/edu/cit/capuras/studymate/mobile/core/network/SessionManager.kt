package edu.cit.capuras.studymate.mobile.core.network

import android.content.Context
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "studymate_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_TOKEN = "token"

    fun saveSession(context: Context, userId: Long, username: String, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_TOKEN, token)
        }
        // Keep the in-memory token holder in sync so ApiClient's interceptor
        // can attach it to every request without needing a Context.
        TokenProvider.token = token
    }

    fun getUserId(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    fun getUsername(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USERNAME, null)
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun isLoggedIn(context: Context): Boolean = getUserId(context) != -1L

    /** Restores the in-memory token holder on app startup (e.g. after process death). */
    fun restoreTokenIntoMemory(context: Context) {
        TokenProvider.token = getToken(context)
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
        TokenProvider.token = null
    }
}

/**
 * Small in-memory holder so the OkHttp interceptor (a singleton with no
 * Android Context) can read the current JWT synchronously on every request.
 */
object TokenProvider {
    @Volatile
    var token: String? = null
}
