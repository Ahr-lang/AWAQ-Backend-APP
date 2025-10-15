package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mawi_app_back.domain.usecase.GetAuthStateUseCase
import com.example.mawi_app_back.domain.usecase.LoginUseCase
import com.example.mawi_app_back.domain.usecase.LogoutUseCase
import com.example.mawi_app_back.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val registerUseCase: RegisterUseCase
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
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de red")
            }
        }
    }

    // Y agrega la función de registro en la clase AuthViewModel
    fun Register(username: String, user_email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                registerUseCase(username, user_email, password)
                _authState.value = AuthState.RegisterSuccess
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