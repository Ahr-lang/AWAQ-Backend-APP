package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.HomeRepository
import com.example.mawi_app_back.presentation.HomeUiState

class GetHomeDataUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): HomeUiState.Success {
        return repository.getHomeData()
    }
}