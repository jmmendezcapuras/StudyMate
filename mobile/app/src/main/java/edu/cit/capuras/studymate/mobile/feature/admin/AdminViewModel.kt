package edu.cit.capuras.studymate.mobile.feature.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.capuras.studymate.mobile.core.network.ApiClient
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    var users by mutableStateOf<List<UserSummaryResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var loadError by mutableStateOf<String?>(null)
        private set

    var actionError by mutableStateOf<String?>(null)
        private set

    var deletingUserId by mutableStateOf<Long?>(null)
        private set

    var isCreating by mutableStateOf(false)
        private set

    var createError by mutableStateOf<String?>(null)
        private set

    var createSuccess by mutableStateOf<String?>(null)
        private set

    fun loadUsers() {
        isLoading = true
        loadError = null
        viewModelScope.launch {
            try {
                val response = ApiClient.adminApi.getAllUsers()
                if (response.isSuccessful) {
                    users = response.body().orEmpty()
                } else {
                    loadError = response.errorBody()?.string() ?: "Couldn't load users."
                }
            } catch (e: Exception) {
                loadError = e.message ?: "Couldn't reach the StudyMate server. Is the backend running?"
            } finally {
                isLoading = false
            }
        }
    }

    fun createAdmin(username: String, email: String, password: String) {
        createError = null
        createSuccess = null

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            createError = "Username, email, and password are required."
            return
        }
        if (password.length < 8) {
            createError = "Password must be at least 8 characters."
            return
        }

        isCreating = true
        viewModelScope.launch {
            try {
                val response = ApiClient.adminApi.createAdmin(
                    CreateAdminRequest(username.trim(), email.trim(), password)
                )
                if (response.isSuccessful) {
                    val body = response.body()!!
                    users = users.plus(body)
                    createSuccess = "Admin account \"${body.username}\" created."
                } else {
                    createError = response.errorBody()?.string() ?: "Couldn't create that admin account."
                }
            } catch (e: Exception) {
                createError = e.message ?: "Couldn't create that admin account."
            } finally {
                isCreating = false
            }
        }
    }

    fun deleteUser(userId: Long) {
        actionError = null
        deletingUserId = userId
        viewModelScope.launch {
            try {
                val response = ApiClient.adminApi.deleteUser(userId)
                if (response.isSuccessful) {
                    users = users.filter { it.id != userId }
                } else {
                    actionError = response.errorBody()?.string() ?: "Couldn't delete that account."
                }
            } catch (e: Exception) {
                actionError = e.message ?: "Couldn't delete that account."
            } finally {
                deletingUserId = null
            }
        }
    }
}
