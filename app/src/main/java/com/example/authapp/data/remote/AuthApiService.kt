package com.example.authapp.data.remote

import com.example.authapp.presentation.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @POST("{tenant}/users/register")
    suspend fun signUp(
        @Path("tenant") tenant: String,
        @Body request: AuthRequest
    ): Response<Unit>

    @POST("{tenant}/users/login")
    suspend fun signIn(
        @Path("tenant") tenant: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @GET("{tenant}/users/me")
    suspend fun getProfile(
        @Path("tenant") tenant: String
    ): Response<ProfileResponse>

    @POST("{tenant}/users/logout")
    suspend fun logout(
        @Path("tenant") tenant: String
    ): Response<Unit>
}

//    @GET("api/users")
//    suspend fun getUsers(): Response<List<User>>

