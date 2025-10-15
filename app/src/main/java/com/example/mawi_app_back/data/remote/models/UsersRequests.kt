package com.example.mawi_app_back.data.remote.models

data class UsersResponse(
    val users: List<UserDto>,
    val tenant: String,
    val count: Int
)

data class UserDto(
    val username: String
)

data class CreateUserRequest(
    val username: String,
    val user_email: String,
    val password: String
)