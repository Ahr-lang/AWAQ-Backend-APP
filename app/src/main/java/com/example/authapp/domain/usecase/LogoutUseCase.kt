package com.example.authapp.domain.usecase

import com.example.authapp.data.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.signOut()
    }
}