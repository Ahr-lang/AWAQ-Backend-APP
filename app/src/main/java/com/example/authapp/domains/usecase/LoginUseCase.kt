package com.example.authapp.domains.usecase

import com.example.authapp.data.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) {
        try {
            repository.signIn(email, password)
        } catch (e: Exception) {
            throw e
        }
    }
}