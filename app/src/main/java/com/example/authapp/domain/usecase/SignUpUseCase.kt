package com.example.authapp.domain.usecase

import com.example.authapp.data.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) {
        try {
            repository.signUp(email, password)
        } catch (e: Exception) {
            // Relanza la excepción para que la capa de presentación la maneje.
            throw e
        }
    }
}