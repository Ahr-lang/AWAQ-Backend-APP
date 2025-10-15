package com.example.mawi_app_back.data.remote.models

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("data") val users: List<UserDto>,
    val tenant: String,
    val count: Int,
    val message: String? = null
)

data class UserDto(
    val id: Int? = null,
    val username: String,
    val user_email: String? = null
)

data class CreateUserRequest(
    val username: String,
    val user_email: String,
    val password: String
)