package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.AuthRepository


class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, user_email: String, password: String) {
        try {
            repository.register(username, user_email, password)
        } catch (e: Exception) {
            // Relanza la excepción para que la capa de presentación la maneje.
            throw e
        }
    }
}