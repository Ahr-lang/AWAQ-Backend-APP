package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.AuthRepository
import com.example.mawi_app_back.presentation.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthStateUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<AuthState> {
        return repository.getAuthToken().map { token ->
            if (token != null) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }
}