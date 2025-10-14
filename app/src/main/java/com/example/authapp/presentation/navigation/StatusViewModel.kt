package com.example.authapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class User(val username: String, val email: String)

class StatusViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _agromoUsers = MutableStateFlow<List<User>>(emptyList())
    val agromoUsers: StateFlow<List<User>> = _agromoUsers

    private val _biomoUsers = MutableStateFlow<List<User>>(emptyList())
    val biomoUsers: StateFlow<List<User>> = _biomoUsers

    private val _roboUsers = MutableStateFlow<List<User>>(emptyList())
    val roboUsers: StateFlow<List<User>> = _roboUsers

    // Función para obtener usuarios por tenant
    fun fetchUsers(tenant: String) {
        viewModelScope.launch {
            try {
                val usersData = repository.getUsersByTenant(tenant)
                val users = usersData.map { User(it.username, it.user_email) }

                when (tenant) {
                    "agromo" -> _agromoUsers.value = users
                    "biomo" -> _biomoUsers.value = users
                    "robo" -> _roboUsers.value = users
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }


    fun addUser(tenant: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.registerWithTenant(username, email, password, tenant)
                fetchUsers(tenant) // refresca la lista
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun deleteUser(tenant: String, email: String) {
        viewModelScope.launch {
            try {
                repository.deleteUser(email, tenant) // Método que tu backend debe soportar
                fetchUsers(tenant)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
