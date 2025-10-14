package com.example.authapp.data.model

data class UserData(
    val id: Int,
    val username: String,
    val user_email: String,
    val lastAccess: String,
    val lastLogin: String,
    val tenant: String
)