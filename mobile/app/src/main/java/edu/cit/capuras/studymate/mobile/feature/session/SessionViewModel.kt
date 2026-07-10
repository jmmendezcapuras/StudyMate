package edu.cit.capuras.studymate.mobile.feature.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.capuras.studymate.mobile.core.network.ApiClient
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {

    var sessions by mutableStateOf<List<SessionResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var loadError by mutableStateOf<String?>(null)
        private set

    var sessionActionError by mutableStateOf<String?>(null)
        private set

    var isSaving by mutableStateOf(false)
        private set

    fun loadData(userId: Long) {
        isLoading = true
        loadError = null
        viewModelScope.launch {
            try {
                val response = ApiClient.sessionApi.getSessions(userId)
                if (response.isSuccessful) {
                    sessions = response.body().orEmpty()
                } else {
                    loadError = "Couldn't load your study sessions."
                }
            } catch (e: Exception) {
                loadError = e.message ?: "Couldn't reach the StudyMate server. Is the backend running?"
            } finally {
                isLoading = false
            }
        }
    }

    fun addSession(userId: Long, subjectId: Long?, durationMinutes: String, sessionDate: String, notes: String) {
        sessionActionError = null

        if (subjectId == null) {
            sessionActionError = "Add a subject first, then log a session against it."
            return
        }
        val duration = durationMinutes.toIntOrNull()
        if (duration == null || duration <= 0) {
            sessionActionError = "Enter a duration greater than 0 minutes."
            return
        }
        if (sessionDate.isBlank()) {
            sessionActionError = "Enter a date (YYYY-MM-DD)."
            return
        }

        isSaving = true
        viewModelScope.launch {
            try {
                val response = ApiClient.sessionApi.addSession(
                    userId,
                    SessionRequest(subjectId, duration, sessionDate, notes.trim().ifBlank { null })
                )
                if (response.isSuccessful) {
                    val body = response.body()!!
                    sessions = listOf(body) + sessions
                } else {
                    sessionActionError = response.errorBody()?.string() ?: "Couldn't log that session."
                }
            } catch (e: Exception) {
                sessionActionError = e.message ?: "Couldn't log that session."
            } finally {
                isSaving = false
            }
        }
    }

    fun deleteSession(userId: Long, sessionId: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.sessionApi.deleteSession(sessionId, userId)
                if (response.isSuccessful) {
                    sessions = sessions.filter { it.id != sessionId }
                } else {
                    loadError = "Couldn't delete that session."
                }
            } catch (e: Exception) {
                loadError = e.message ?: "Couldn't delete that session."
            }
        }
    }
}
