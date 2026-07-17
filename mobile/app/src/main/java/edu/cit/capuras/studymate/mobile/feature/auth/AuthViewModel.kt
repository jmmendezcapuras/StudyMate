package edu.cit.capuras.studymate.mobile.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.capuras.studymate.mobile.core.network.ApiClient
import kotlinx.coroutines.launch

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val response: AuthResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun register(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            uiState = AuthUiState.Error("Username, email, and password are required")
            return
        }

        uiState = AuthUiState.Loading
        viewModelScope.launch {
            try {
                val response = ApiClient.authApi.register(RegisterRequest(username, email, password))
                if (response.isSuccessful && response.body() != null) {
                    uiState = AuthUiState.Success(response.body()!!)
                } else {
                    uiState = AuthUiState.Error(response.errorBody()?.string() ?: "Registration failed")
                }
            } catch (e: Exception) {
                uiState = AuthUiState.Error(e.message ?: "Network error. Is the backend running?")
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            uiState = AuthUiState.Error("Username and password are required")
            return
        }

        uiState = AuthUiState.Loading
        viewModelScope.launch {
            try {
                val response = ApiClient.authApi.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    uiState = AuthUiState.Success(response.body()!!)
                } else {
                    uiState = AuthUiState.Error(response.errorBody()?.string() ?: "Invalid credentials")
                }
            } catch (e: Exception) {
                uiState = AuthUiState.Error(e.message ?: "Network error. Is the backend running?")
            }
        }
    }

    fun resetState() {
        uiState = AuthUiState.Idle
    }
}
