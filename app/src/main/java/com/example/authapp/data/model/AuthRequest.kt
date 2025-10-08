package com.example.authapp.data.model

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val message: String,
    val token: String
)

data class ProfileResponse(
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val email: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val user_email: String
)