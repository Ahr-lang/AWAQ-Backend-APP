package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.ErrorsRepository
import com.example.mawi_app_back.presentation.ErrorItem

class GetErrorsUseCase(
    private val repo: ErrorsRepository
) {
    suspend operator fun invoke(tenant: String): List<ErrorItem> {
        return repo.getTenantErrors(tenant)
    }
}