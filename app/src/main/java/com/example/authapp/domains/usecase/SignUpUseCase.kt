package com.example.authapp.domains.usecase

import com.example.authapp.data.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) {
        try {
            repository.register(email, email, password, "back")
        } catch (e: Exception) {
            // Relanza la excepción para que la capa de presentación la maneje.
            throw e
        }
    }
}