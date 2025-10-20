package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.UsersRepository

class DeleteUserUseCase(private val repo: UsersRepository) {
    suspend operator fun invoke(tenant: String, userId: Int) =
        repo.deleteUser(tenant, userId)
}