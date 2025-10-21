package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mawi_app_back.domain.usecase.GetHomeDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        // Don't load data automatically to prevent crashes
        // Data will be loaded when the UI is ready
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                val data = getHomeDataUseCase()
                _uiState.value = data
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al cargar datos: ${e.localizedMessage ?: "Error desconocido"}")
            }
        }
    }

    fun refresh() {
        loadData()
    }
}