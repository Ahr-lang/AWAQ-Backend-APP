package com.example.mawi_app_back.data.remote

data class LoginRequest(
    val user_email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String
)

data class RegisterRequest(
    val username: String,
    val user_email: String,
    val password: String
)
data class RegisterResponse(
    val message: String
)

data class ProfileResponse(
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val email: String
)
