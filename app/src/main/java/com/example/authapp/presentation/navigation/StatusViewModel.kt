package com.example.authapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class User(
    val username: String,
    val email: String
)

class StatusViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _agromoUsers = MutableStateFlow<List<User>>(emptyList())
    val agromoUsers: StateFlow<List<User>> = _agromoUsers

    private val _biomoUsers = MutableStateFlow<List<User>>(emptyList())
    val biomoUsers: StateFlow<List<User>> = _biomoUsers

    private val _roboUsers = MutableStateFlow<List<User>>(emptyList())
    val roboUsers: StateFlow<List<User>> = _roboUsers


    // 🔹 Obtener usuarios por tenant
    fun fetchUsers(tenant: String) {
        viewModelScope.launch {
            try {
                val usersData = repository.getUsersByTenant(tenant)
                val users = usersData.map {
                    User(
                        username = it.username,
                        email = it.user_email
                    )
                }

                when (tenant.lowercase()) {
                    "agromo" -> _agromoUsers.value = users
                    "biomo" -> _biomoUsers.value = users
                    "robo" -> _roboUsers.value = users
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🔹 Agregar usuario (usa el backend real)
    fun addUser(tenant: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.registerWithTenant(username, email, password, tenant)
                fetchUsers(tenant) // Refresca lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteUser(tenant: String, email: String) {
        viewModelScope.launch {
            try {
                repository.deleteUser(email, tenant)
                fetchUsers(tenant)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
