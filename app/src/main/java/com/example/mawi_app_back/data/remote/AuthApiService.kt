package com.example.mawi_app_back.data.remote

import com.example.mawi_app_back.data.remote.models.CreateUserRequest
import com.example.mawi_app_back.data.remote.models.UsersResponse
import retrofit2.Response
import retrofit2.http.*
import com.example.mawi_app_back.presentation.StatusResponse
import com.example.mawi_app_back.presentation.ErrorsResponse
import com.example.mawi_app_back.presentation.*

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

    @GET("api/{tenant}/admin/status")
    suspend fun getAdminStatus(
        @Path("tenant") tenant: String
    ): Response<StatusResponse>

    @GET("api/{tenant}/admin/errors")
    suspend fun getAdminErrors(
        @Path("tenant") tenant: String
    ): Response<ErrorsResponse>

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
    @DELETE("api/{tenant}/admin/users/{userId}")
    suspend fun deleteUser(
        @Path("tenant") tenant: String,
        @Path("userId") userId: Int
    ): Response<Unit>

    // HomeScreen Admin Endpoints
    @GET("api/{tenant}/admin/users/top-by-form-type")
    suspend fun getTopUsersByFormType(
        @Path("tenant") tenant: String
    ): Response<List<TopUserByFormType>>

    @GET("api/{tenant}/admin/metrics/form")
    suspend fun getFormMetrics(
        @Path("tenant") tenant: String
    ): Response<List<FormMetrics>>

    @GET("api/{tenant}/admin/metrics/online-users")
    suspend fun getOnlineUsers(
        @Path("tenant") tenant: String
    ): Response<OnlineUsersResponse>

    @GET("api/admin/metrics/online-users/total")
    suspend fun getTotalOnlineUsers(): Response<TotalOnlineUsersResponse>

    @GET("api/admin/metrics/form")
    suspend fun getAllFormMetrics(): Response<FormMetricsApiResponse>
}
