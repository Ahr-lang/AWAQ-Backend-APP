package com.example.authapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.authapp.data.remote.ApiClient
import com.example.authapp.presentation.User
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val _usersByApp = MutableLiveData<Map<String, List<User>>>(emptyMap())
    val usersByApp: LiveData<Map<String, List<User>>> = _usersByApp

/*
    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getUsers()
                if (response.isSuccessful) {
                    val allUsers: List<User> = response.body() ?: emptyList()

                    // Agrupa por app; si app pudiera ser null usa it.app ?: "Unknown"
                    val grouped: Map<String, List<User>> = allUsers.groupBy { it.app }
                    _usersByApp.postValue(grouped)
                } else {
                    // Puedes publicar un mapa vacío o manejar el error según tu UI
                    _usersByApp.postValue(emptyMap())
                    println("Error en fetchUsers: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _usersByApp.postValue(emptyMap())
                println("Excepción en fetchUsers: ${e.message}")
            }
        }
    }

*/
}