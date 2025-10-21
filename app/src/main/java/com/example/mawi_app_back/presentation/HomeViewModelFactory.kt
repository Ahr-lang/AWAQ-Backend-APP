package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mawi_app_back.domain.usecase.GetHomeDataUseCase

class HomeViewModelFactory(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getHomeDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}