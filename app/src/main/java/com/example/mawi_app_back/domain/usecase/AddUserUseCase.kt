package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.UsersRepository

class AddUserUseCase(private val repo: UsersRepository) {
    suspend operator fun invoke(tenant: String, username: String, userEmail: String, password: String) =
        repo.addUser(tenant, username, userEmail, password)
}