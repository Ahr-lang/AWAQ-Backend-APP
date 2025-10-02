package com.example.authapp.domain.usecase

import com.example.authapp.data.AuthRepository
import com.example.authapp.presentation.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthStateUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<AuthState> {
        return repository.getAuthToken().map { token ->
            if (token != null) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }
}