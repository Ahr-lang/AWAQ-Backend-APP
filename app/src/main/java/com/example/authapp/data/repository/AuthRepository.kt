package com.example.authapp.data.repository

import com.example.authapp.global.util.TokenManager
import com.example.authapp.data.model.FormRequest
import com.example.authapp.data.remote.RegisterRequest
import com.example.authapp.data.model.TodoDto
import com.example.authapp.data.remote.AuthApiService
import com.example.authapp.data.remote.LoginRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun signIn(email: String, password: String) {
        try {
            // Llamada suspend a Retrofit
            val response = apiService.login(LoginRequest(email, password))

            // Guardamos el token usando TokenManager
            tokenManager.saveToken(response.token)

        } catch (e: Exception) {
            // Lanza la excepción para que la capa de presentación la maneje
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    suspend fun register(username: String, email: String, password: String) {
        val request = RegisterRequest(
            username = username,
            user_email = email,
            password = password
        )
        val response = apiService.register(request) // solo 1 parámetro
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("Registro fallido: ${response.message()}")
        }
    }
    suspend fun signOut() {
        tokenManager.deleteToken()
    }

    fun getAuthToken(): Flow<String?> {
        return tokenManager.token
    }

    suspend fun submitForm(field1: String) {
        val formRequest = FormRequest(field1)
        apiService.submitForm(formRequest)
    }

    suspend fun getTodos(): Result<List<TodoDto>> {
        return try {
            val response = apiService.getTodos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error obteniendo tareas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}