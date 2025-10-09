package com.example.authapp.domains.usecase

import com.example.authapp.data.model.TodoDto
import com.example.authapp.data.repository.AuthRepository

class GetTodosUseCase(private val todoRepository: AuthRepository) {
    suspend operator fun invoke(): Result<List<TodoDto>> {
        return todoRepository.getTodos().map { it }
    }
}