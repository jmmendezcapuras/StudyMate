package edu.cit.capuras.studymate.mobile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.capuras.studymate.mobile.network.ApiClient
import edu.cit.capuras.studymate.mobile.network.dto.SubjectRequest
import edu.cit.capuras.studymate.mobile.network.dto.SubjectResponse
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    var subjects by mutableStateOf<List<SubjectResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var loadError by mutableStateOf<String?>(null)
        private set

    var subjectActionError by mutableStateOf<String?>(null)
        private set

    var isAddingSubject by mutableStateOf(false)
        private set

    fun loadData(userId: Long) {
        isLoading = true
        loadError = null
        viewModelScope.launch {
            try {
                val subjectsResponse = ApiClient.subjectApi.getSubjects(userId)
                if (subjectsResponse.isSuccessful) {
                    subjects = subjectsResponse.body().orEmpty()
                } else {
                    loadError = "Couldn't load your StudyMate data."
                }
            } catch (e: Exception) {
                loadError = e.message ?: "Couldn't reach the StudyMate server. Is the backend running?"
            } finally {
                isLoading = false
            }
        }
    }

    fun addSubject(userId: Long, name: String) {
        subjectActionError = null
        if (name.isBlank()) {
            subjectActionError = "Give the subject a name first."
            return
        }

        isAddingSubject = true
        viewModelScope.launch {
            try {
                val response = ApiClient.subjectApi.addSubject(userId, SubjectRequest(name.trim()))
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val updated = subjects.toMutableList()
                    updated.add(body)
                    subjects = updated
                } else {
                    subjectActionError = response.errorBody()?.string() ?: "Couldn't add that subject."
                }
            } catch (e: Exception) {
                subjectActionError = e.message ?: "Couldn't add that subject."
            } finally {
                isAddingSubject = false
            }
        }
    }
}
