package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.data.remote.models.CreateUserRequest
import com.example.mawi_app_back.data.remote.models.UserDto

class UsersRepository(
    private val api: AuthApiService
) {
    suspend fun getUsersByTenant(tenant: String): List<UserDto> {
        val resp = api.getAdminUsers(tenant)
        if (resp.isSuccessful) return resp.body()?.users ?: emptyList()
        error("Error ${resp.code()}: ${resp.message()}")
    }

    suspend fun addUser(tenant: String, username: String, userEmail: String, password: String) {
        val resp = api.createAdminUser(tenant, CreateUserRequest(username, userEmail, password))
        if (!resp.isSuccessful) error("Error ${resp.code()}: ${resp.message()}")
    }

    suspend fun deleteUser(tenant: String, username: String) {
        val resp = api.deleteAdminUser(tenant, username)
        if (!resp.isSuccessful) error("Error ${resp.code()}: ${resp.message()}")
    }
}
