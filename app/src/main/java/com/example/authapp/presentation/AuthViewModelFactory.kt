package com.example.authapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.data.AuthRepository
import com.example.authapp.domain.usecase.GetAuthStateUseCase
import com.example.authapp.domain.usecase.LoginUseCase
import com.example.authapp.domain.usecase.LogoutUseCase
import com.example.authapp.domain.usecase.SignUpUseCase

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val loginUseCase = LoginUseCase(authRepository)
            val logoutUseCase = LogoutUseCase(authRepository)
            val getAuthStateUseCase = GetAuthStateUseCase(authRepository)
            val signUpUseCase = SignUpUseCase(authRepository)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(loginUseCase, logoutUseCase, getAuthStateUseCase, signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}