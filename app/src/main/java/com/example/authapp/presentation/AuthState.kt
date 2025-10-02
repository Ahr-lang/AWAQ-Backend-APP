package com.example.authapp.presentation

sealed class AuthState {
    object InitialLoading : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object SignUpSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}