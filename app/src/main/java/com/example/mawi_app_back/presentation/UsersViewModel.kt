package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mawi_app_back.data.remote.models.UserDto
import com.example.mawi_app_back.domain.usecase.AddUserUseCase
import com.example.mawi_app_back.domain.usecase.DeleteUserUseCase
import com.example.mawi_app_back.domain.usecase.GetUsersByTenantUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UsersUiState {
    data object Idle : UsersUiState
    data object Loading : UsersUiState
    data class Success(val users: List<UserDto>, val tenant: String, val message: String? = null) : UsersUiState
    data class Error(val message: String) : UsersUiState
}

class UsersViewModel(
    private val getUsersByTenant: GetUsersByTenantUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersUiState>(UsersUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val tenantsInternal = listOf("back", "agromo", "biomo", "robo")
    var currentTenant: String = tenantsInternal.first()
        private set

    val tenants: List<String> get() = tenantsInternal

    fun setTenant(tenant: String) {
        if (tenant == currentTenant) return
        currentTenant = tenant
        fetchUsers()
    }

    fun fetchUsers(msg: String? = null) {
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            try {
                val data = getUsersByTenant(currentTenant)
                _uiState.value = UsersUiState.Success(data, currentTenant, msg)
            } catch (e: Exception) {
                _uiState.value = UsersUiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun addUser(username: String, email: String, password: String) {
        when {
            username.isBlank() -> {
                _uiState.value = UsersUiState.Error("Username is required")
                return
            }
            email.isBlank() -> {
                _uiState.value = UsersUiState.Error("Email is required")
                return
            }
            password.isBlank() -> {
                _uiState.value = UsersUiState.Error("Password is required")
                return
            }
        }
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            try {
                addUserUseCase(currentTenant, username, email, password)
                fetchUsers("User \"$username\" created")
            } catch (e: Exception) {
                _uiState.value = UsersUiState.Error(e.message ?: "Create failed")
            }
        }
    }

    fun deleteUser(username: String) {
        if (username.isBlank()) {
            _uiState.value = UsersUiState.Error("Username is required")
            return
        }
        viewModelScope.launch {
            _uiState.value = UsersUiState.Loading
            try {
                deleteUserUseCase(currentTenant, username)
                fetchUsers("User \"$username\" deleted")
            } catch (e: Exception) {
                _uiState.value = UsersUiState.Error(e.message ?: "Delete failed")
            }
        }
    }
}
