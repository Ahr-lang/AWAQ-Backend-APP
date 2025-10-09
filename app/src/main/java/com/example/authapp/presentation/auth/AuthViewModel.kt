package com.example.authapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.domains.usecase.GetAuthStateUseCase
import com.example.authapp.domains.usecase.LoginUseCase
import com.example.authapp.domains.usecase.LogoutUseCase
import com.example.authapp.domains.usecase.SignUpUseCase
import com.example.authapp.presentation.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.InitialLoading)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthStateUseCase().collect { state ->
                _authState.value = state
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                loginUseCase(email, password)
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de red")
            }
        }
    }

    // Y agrega la función de registro en la clase AuthViewModel
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                authRepository.register(username, email, password)
                _authState.value = AuthState.SignUpSuccess
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de registro")
            }
        }
    }

    fun resetState() {
        // Resetting to Idle or Unauthenticated state
        _authState.value = AuthState.Unauthenticated
    }

    fun signOut() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}