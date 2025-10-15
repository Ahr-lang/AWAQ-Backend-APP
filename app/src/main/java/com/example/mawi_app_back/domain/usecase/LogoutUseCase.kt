package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.AuthRepository


class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.signOut()
    }
}