package com.example.authapp.domain.usecase

import com.example.authapp.data.AuthRepository
import com.example.authapp.data.remote.TodoDto

class GetTodosUseCase(private val todoRepository: AuthRepository) {
    suspend operator fun invoke(): Result<List<TodoDto>> {
        return todoRepository.getTodos().map { it }
    }
}
