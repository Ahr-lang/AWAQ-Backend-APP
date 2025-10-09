package com.example.authapp.domains.usecase

import com.example.authapp.data.repository.AuthRepository

class SubmitFormUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(field1: String) {
        // Aquí podrías añadir validaciones antes de enviar
        if (field1.isBlank()) {
            throw IllegalArgumentException("El campo 1 no puede estar vacío.")
        }
        authRepository.submitForm(field1)
    }
}