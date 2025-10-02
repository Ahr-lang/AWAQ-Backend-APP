package com.example.authapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.domain.usecase.SubmitFormUseCase
import kotlinx.coroutines.launch

class FormViewModel(private val submitFormUseCase: SubmitFormUseCase) : ViewModel() {
    // Aquí podrías añadir estados para la UI (ej. loading, success, error)

    fun submit(field1: String) {
        viewModelScope.launch {
            try {
                submitFormUseCase(field1)
                // Actualizar el estado a "success"
            } catch (e: Exception) {
                // Actualizar el estado a "error"
            }
        }
    }
}