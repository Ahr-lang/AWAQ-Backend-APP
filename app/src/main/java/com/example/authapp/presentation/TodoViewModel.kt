package com.example.authapp.presentation

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.remote.TodoDto
import com.example.authapp.domain.usecase.GetTodosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TodoListState {
    object Loading : TodoListState()
    data class Success(val todos: List<TodoDto>) : TodoListState()
    data class Error(val message: String) : TodoListState()
    object Idle : TodoListState() // Estado inicial o después de una operación
}

sealed class TodoActionState { // Para operaciones individuales como añadir, actualizar, borrar
    object Loading : TodoActionState()
    object Success : TodoActionState()
    data class Error(val message: String) : TodoActionState()
    object Idle : TodoActionState()
}

class TodoViewModel( private val getTodosUseCase: GetTodosUseCase ) : ViewModel() {

    private val _todoListState = MutableStateFlow<TodoListState>(TodoListState.Idle)
    val todoListState: StateFlow<TodoListState> = _todoListState.asStateFlow()


    fun loadTodos() {
        viewModelScope.launch() {
            _todoListState.value = TodoListState.Loading
            getTodosUseCase().onSuccess { todos ->
                _todoListState.value = TodoListState.Success(todos)
            }.onFailure { error ->
                _todoListState.value = TodoListState.Error(error.message ?: "Error desconocido")
            }
        }
    }
}