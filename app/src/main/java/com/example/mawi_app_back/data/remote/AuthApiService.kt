package com.example.mawi_app_back.data.remote

import com.example.mawi_app_back.data.remote.models.CreateUserRequest
import com.example.mawi_app_back.data.remote.models.UsersResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("api/back/users/login")
    suspend fun logIn(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/back/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // Admin: list ALL users for a tenant
    @GET("api/{tenant}/admin/users")
    suspend fun getAdminUsers(
        @Path("tenant") tenant: String
    ): Response<UsersResponse>

    @POST("api/{tenant}/admin/users")
    suspend fun createAdminUser(
        @Path("tenant") tenant: String,
        @Body req: CreateUserRequest
    ): Response<Unit>

    @DELETE("api/{tenant}/admin/users/{username}")
    suspend fun deleteAdminUser(
        @Path("tenant") tenant: String,
        @Path("username") username: String
    ): Response<Unit>

    // admin - borrar usuario, checar
    @DELETE("/api/{tenant}/admin/users/{userId}")
    suspend fun deleteUser(
        @Path("tenant") tenant: String,
        @Path("userId") userId: Int
    ): Response<Unit>
}
