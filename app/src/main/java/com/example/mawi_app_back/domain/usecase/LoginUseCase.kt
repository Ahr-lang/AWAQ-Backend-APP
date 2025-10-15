package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(user_email: String, password: String) {
        try {
            repository.logIn(user_email, password)
        } catch (e: Exception) {
            throw e
        }
    }
}