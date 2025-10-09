package com.example.authapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.domains.usecase.GetTodosUseCase

class TodoViewModelFactory(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(getTodosUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}