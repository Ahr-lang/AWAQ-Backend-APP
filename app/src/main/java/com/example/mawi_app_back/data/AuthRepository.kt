package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.local.TokenManager
import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.data.remote.LoginRequest
import com.example.mawi_app_back.data.remote.RegisterRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun logIn(email: String, password: String) {
        val response = apiService.logIn("agromo", LoginRequest(email, password))
        if (response.isSuccessful && response.body() != null) {
            val token = response.body()!!.token
            tokenManager.saveToken(token)
        } else {
            throw Exception("Credenciales inválidas o error del servidor.")
        }
    }

    suspend fun register(username: String, user_email: String, password: String) {
        val response = apiService.register("agromo", RegisterRequest(username, user_email, password))
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

    // admin - borrar usuario checar

    suspend fun deleteUser(tenant: String, userId: Int): Boolean {
        return try {
            val response = apiService.deleteAdminUserById(tenant, userId)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}