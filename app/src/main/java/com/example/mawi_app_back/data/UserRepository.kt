package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.data.remote.models.CreateUserRequest
import com.example.mawi_app_back.data.remote.models.UserDto

class UsersRepository(
    private val api: AuthApiService
) {
    // Fetch all users for a tenant
    suspend fun getUsersByTenant(tenant: String): List<UserDto> {
        val resp = api.getAdminUsers(tenant)
        if (resp.isSuccessful) return resp.body()?.users ?: emptyList()
        error("Error ${resp.code()}: ${resp.message()}")
    }

    // Create new user
    suspend fun addUser(tenant: String, username: String, userEmail: String, password: String) {
        val resp = api.createAdminUser(tenant, CreateUserRequest(username, userEmail, password))
        if (!resp.isSuccessful) error("Error ${resp.code()}: ${resp.message()}")
    }

    // ✅ Delete user by ID (matches your backend endpoint)
    suspend fun deleteUser(tenant: String, userId: Int): Boolean {
        println("🟢 Deleting user ID $userId from tenant $tenant")
        return try {
            val resp = api.deleteAdminUserById(tenant, userId)
            println("🟢 Response code: ${resp.code()}")
            if (!resp.isSuccessful) {
                error("Error ${resp.code()}: ${resp.message()}")
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}



