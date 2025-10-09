package com.example.authapp.domains.usecase

import com.example.authapp.data.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.signOut()
    }
}