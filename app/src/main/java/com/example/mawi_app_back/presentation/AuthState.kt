package com.example.mawi_app_back.presentation

sealed class AuthState {
    object InitialLoading : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object RegisterSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}