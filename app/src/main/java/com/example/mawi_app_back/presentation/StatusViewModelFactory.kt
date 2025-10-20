package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mawi_app_back.domain.usecase.GetStatusDashboardUseCase

class StatusViewModelFactory(
    private val getStatusUC: GetStatusDashboardUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
            return StatusViewModel(getStatusUC) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}