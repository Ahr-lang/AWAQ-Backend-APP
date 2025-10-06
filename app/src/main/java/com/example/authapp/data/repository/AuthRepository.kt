package com.example.authapp.data.repository

import com.example.authapp.global.util.TokenManager
import com.example.authapp.data.model.AuthRequest
import com.example.authapp.data.model.FormRequest
import com.example.authapp.data.model.TodoDto
import com.example.authapp.data.remote.AuthApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun signIn(email: String, password: String) {
        val response = apiService.signIn(AuthRequest(email, password))
        if (response.isSuccessful && response.body() != null) {
            val token = response.body()!!.token
            tokenManager.saveToken(token)
        } else {
            throw Exception("Credenciales inválidas o error del servidor.")
        }
    }
    suspend fun signUp(email: String, password: String) {
        val response = apiService.signUp(AuthRequest(email, password))
        if (!response.isSuccessful) {
            // Maneja el error, por ejemplo, si el usuario ya existe
            throw Exception("El registro falló: ${response.message()}")
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