package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.UsersRepository
import com.example.mawi_app_back.data.remote.models.UserDto

class GetUsersByTenantUseCase(
    private val repo: UsersRepository
) {
    suspend operator fun invoke(tenant: String): List<UserDto> =
        repo.getUsersByTenant(tenant)
}
