package edu.cit.capuras.studymate.mobile.core.network

import android.content.Context
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "studymate_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    fun saveSession(context: Context, userId: Long, username: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
        }
    }

    fun getUserId(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    fun getUsername(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USERNAME, null)
    }

    fun isLoggedIn(context: Context): Boolean = getUserId(context) != -1L

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }
}
