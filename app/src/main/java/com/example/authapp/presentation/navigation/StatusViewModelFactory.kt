package com.example.authapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.data.repository.AuthRepository

class StatusViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
