package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mawi_app_back.domain.usecase.GetAuthStateUseCase
import com.example.mawi_app_back.domain.usecase.LoginUseCase
import com.example.mawi_app_back.domain.usecase.LogoutUseCase
import com.example.mawi_app_back.domain.usecase.RegisterUseCase
import com.example.mawi_app_back.data.AuthRepository

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val loginUseCase = LoginUseCase(authRepository)
            val logoutUseCase = LogoutUseCase(authRepository)
            val getAuthStateUseCase = GetAuthStateUseCase(authRepository)
            val signUpUseCase = RegisterUseCase(authRepository)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(loginUseCase, logoutUseCase, getAuthStateUseCase, signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}