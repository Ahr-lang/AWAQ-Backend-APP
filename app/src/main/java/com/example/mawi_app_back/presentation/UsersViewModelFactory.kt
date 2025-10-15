package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mawi_app_back.domain.usecase.AddUserUseCase
import com.example.mawi_app_back.domain.usecase.DeleteUserUseCase
import com.example.mawi_app_back.domain.usecase.GetUsersByTenantUseCase

class UsersViewModelFactory(
    private val getUsersByTenant: GetUsersByTenantUseCase,
    private val addUser: AddUserUseCase,
    private val deleteUser: DeleteUserUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(getUsersByTenant, addUser, deleteUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
