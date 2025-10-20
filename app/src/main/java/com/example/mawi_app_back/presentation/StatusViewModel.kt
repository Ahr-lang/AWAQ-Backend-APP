package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mawi_app_back.domain.usecase.GetStatusDashboardUseCase
import com.example.mawi_app_back.domain.usecase.TenantStatusRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface StatusUiState {
    data object Idle : StatusUiState
    data object Loading : StatusUiState
    data class Success(val rows: List<TenantStatusRow>) : StatusUiState
    data class Error(val message: String) : StatusUiState
}

class StatusViewModel(
    private val getStatus: GetStatusDashboardUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<StatusUiState>(StatusUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = StatusUiState.Loading
            try {
                val data = getStatus()
                _uiState.value = StatusUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = StatusUiState.Error(e.message ?: "Network error")
            }
        }
    }
}