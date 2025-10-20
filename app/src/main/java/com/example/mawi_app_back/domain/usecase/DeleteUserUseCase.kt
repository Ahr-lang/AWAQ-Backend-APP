package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.UsersRepository

class DeleteUserUseCase(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(tenant: String, username: String) {
        repository.deleteUser(tenant, username)
    }
}