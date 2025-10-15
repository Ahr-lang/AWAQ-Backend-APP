package com.example.authapp.data.remote

import com.example.authapp.data.model.FormRequest
import com.example.authapp.data.model.ProfileResponse
import com.example.authapp.data.model.TodoDto
import com.example.authapp.presentation.users.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

// AGREGUE ESTO
data class RegisterResponse(
    val message: String,
    val user_id: String? = null
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val tenant: String
)

interface AuthApiService {
    @POST("/api/back/users/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<UserData>

    @POST("api/back/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    //AGREGUE ESTO PROLLY CAMBIAR LO DE {TENANT}
    @POST("api/auth/register/{tenant}")
    suspend fun register(
        @Path("tenant") tenant: String,
        @Body request: RegisterRequest
    ): Response<RegisterResponse> //VER QUE DEVUELVE LA API PARA DEFINIRLO LUEGO

    // AGREGUE ESTO AGUAS CON ESTO
    @GET("api/{tenant}/users")
    suspend fun getUsersByTenant(
        @Path("tenant") tenant: String
    ): Response<List<UserData>>
    // ESTO TMB AGUAS CON ESTO
    @DELETE("api/{tenant}/users/{email}")
    suspend fun deleteUser(
        @Path("tenant") tenant: String,
        @Path("email") email: String
    ): Response<Unit>

    @GET("api/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("api/todos") // o la ruta que corresponda en tu backend
    suspend fun submitForm(@Body formRequest: FormRequest): Response<Unit> // Asumimos que el backend no devuelve nada en el cuerpo

    @GET("api/todos")
    suspend fun getTodos(): Response<List<TodoDto>>

    @GET("api/users")
    suspend fun getUsers(): Response<List<User>>

}