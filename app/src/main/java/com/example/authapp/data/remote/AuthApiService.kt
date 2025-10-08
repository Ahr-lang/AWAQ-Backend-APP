package com.example.authapp.data.remote

import com.example.authapp.data.model.FormRequest
import com.example.authapp.data.model.ProfileResponse
import com.example.authapp.data.model.TodoDto
import com.example.authapp.presentation.users.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Path

data class LoginRequest(
    val user_email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String,
    val token_type: String,
    val expires_in: String,
    val user: UserData
)

data class UserData(
    val id: Int,
    val username: String,
    val user_email: String,
    val lastAccess: String,
    val lastLogin: String,
    val tenant: String
)

data class RegisterRequest(
    val username: String,
    val user_email: String,
    val password: String
)

interface AuthApiService {
    @POST("/api/back/users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<UserData>

    @POST("api/back/users/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("api/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("api/todos") // o la ruta que corresponda en tu backend
    suspend fun submitForm(@Body formRequest: FormRequest): Response<Unit> // Asumimos que el backend no devuelve nada en el cuerpo

    @GET("api/todos")
    suspend fun getTodos(): Response<List<TodoDto>>

    @GET("api/users")
    suspend fun getUsers(): Response<List<User>>

}