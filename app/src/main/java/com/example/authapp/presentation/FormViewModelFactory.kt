package com.example.authapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.domain.usecase.SubmitFormUseCase

class FormViewModelFactory(private val submitFormUseCase: SubmitFormUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormViewModel(submitFormUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
