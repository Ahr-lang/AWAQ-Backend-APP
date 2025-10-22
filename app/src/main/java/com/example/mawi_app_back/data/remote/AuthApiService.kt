package com.example.mawi_app_back.data.remote

import com.example.mawi_app_back.data.remote.models.CreateUserRequest
import com.example.mawi_app_back.data.remote.models.UsersResponse
import retrofit2.Response
import retrofit2.http.*
import com.example.mawi_app_back.presentation.StatusResponse
import com.example.mawi_app_back.presentation.ErrorsResponse
import com.example.mawi_app_back.presentation.*

interface AuthApiService {

    // ---------- Auth ----------
    @POST("api/{tenant}/users/login")
    suspend fun logIn(
        @Path("tenant") tenant: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/{tenant}/users/register")
    suspend fun register(
        @Path("tenant") tenant: String,
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    // ---------- Admin: Users ----------
    @GET("api/{tenant}/admin/users")
    suspend fun getAdminUsers(
        @Path("tenant") tenant: String
    ): Response<UsersResponse>

    @POST("api/{tenant}/admin/users")
    suspend fun createAdminUser(
        @Path("tenant") tenant: String,
        @Body req: CreateUserRequest
    ): Response<Unit>

    // Para evitar colisión de rutas /users/{algo}, separo por id/username:
    @DELETE("api/{tenant}/admin/users/by-username/{username}")
    suspend fun deleteAdminUserByUsername(
        @Path("tenant") tenant: String,
        @Path("username") username: String
    ): Response<Unit>

    @DELETE("api/{tenant}/admin/users/{userId}")
    suspend fun deleteAdminUserById(
        @Path("tenant") tenant: String,
        @Path("userId") userId: Int
    ): Response<Unit>

    // ---------- Admin: Status / Errors ----------
    @GET("api/{tenant}/admin/status")
    suspend fun getAdminStatus(
        @Path("tenant") tenant: String
    ): Response<StatusResponse>

    @GET("api/{tenant}/admin/errors")
    suspend fun getAdminErrors(
        @Path("tenant") tenant: String
    ): Response<ErrorsResponse>

    // ---------- Home / Métricas ----------
    @GET("api/{tenant}/admin/metrics/online-users")
    suspend fun getOnlineUsers(
        @Path("tenant") tenant: String = "back"
    ): Response<OnlineUsersApiResponse>

    // Removido: endpoint total no existe en Swagger
    // @GET("api/admin/metrics/online-users/total")
    // suspend fun getTotalOnlineUsers(): Response<TotalOnlineUsersResponse>

    @GET("api/{tenant}/admin/metrics/forms")
    suspend fun getFormMetrics(
        @Path("tenant") tenant: String
    ): Response<List<FormMetrics>>

    @GET("api/admin/metrics/forms")
    suspend fun getAllFormMetrics(): Response<FormMetricsApiResponse>

    // ---------- FORMS (los 2 que vamos a usar) ----------

    // 1) Usuarios con conteo de formularios (solo backend users)
    //    Devuelve una lista (ajusta el tipo si tu modelo es otro)
    @GET("api/{tenant}/admin/users/forms")
    suspend fun getUsersWithFormCounts(
        @Path("tenant") tenant: String
    ): Response<UsersWithFormsApiResponse>

    // 2) Top user por tipo de formulario (solo backend users)
    @GET("api/{tenant}/admin/users/top-by-form-type")
    suspend fun getTopUsersByFormType(
        @Path("tenant") tenant: String
    ): Response<TopUsersByFormTypeApiResponse>

}
