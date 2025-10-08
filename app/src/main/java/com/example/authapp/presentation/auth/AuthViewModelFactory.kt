package com.example.authapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.domains.usecase.GetAuthStateUseCase
import com.example.authapp.domains.usecase.LoginUseCase
import com.example.authapp.domains.usecase.LogoutUseCase
import com.example.authapp.domains.usecase.SignUpUseCase

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
            return AuthViewModel(
                loginUseCase,
                logoutUseCase,
                getAuthStateUseCase,
                signUpUseCase,
                authRepository // <--- agregado para register
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}