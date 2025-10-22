package com.example.mawi_app_back.presentation

import com.example.mawi_app_back.data.remote.models.UserDto

// Data models for HomeScreen API responses

// Users with total form counts
data class UserWithFormCount(
    val id: Int,
    val username: String,
    val user_email: String,
    val forms_count: Int
)

data class UsersWithFormsApiResponse(
    val message: String,
    val tenant: String,
    val data: List<UserWithFormCount>,
    val count: Int
)

data class UsersWithFormsResponse(
    val tenant: String,
    val users: List<UserWithFormCount>
)

data class UserFormsCount(
    val userId: Int,
    val username: String,
    val email: String,
    val formCount: Int
)

data class TopUserByFormType(
    val form_type: String,
    val user_id: Int,
    val username: String,
    val user_email: String,
    val count: Int
) {
    // Helper properties for backwards compatibility
    val formType: String get() = form_type
    val formCount: Int get() = count
    val user: UserDto get() = UserDto(user_id, username, user_email)
}

// API response wrappers
data class TopUsersByFormTypeApiResponse(
    val message: String,
    val tenant: String,
    val data: List<TopUserByFormType>,
    val count: Int
)

data class TopUsersByFormTypeResponse(
    val tenant: String,
    val topUsers: List<TopUserByFormType>
)

data class FormMetrics(
    val formType: String,
    val count: Int
)

data class FormMetricsResponse(
    val tenant: String,
    val metrics: List<FormMetrics>
)

data class FormMetricItem(
    val tenant: String,
    val form_type: String,
    val count: Int
)

data class FormMetricsApiResponse(
    val success: Boolean,
    val data: List<FormMetricItem>,
    val timestamp: String
)

data class OnlineUser(
    val id: Int,
    val username: String,
    val email: String,
    val lastActivity: String? = null
)

data class OnlineUsersResponse(
    val tenant: String?,
    val count: Int,
    val users: List<OnlineUser>? = emptyList()
)

data class TotalOnlineUsersResponse(
    val total: Int,
    val byTenant: Map<String, Int>
)

data class OnlineUsersApiResponse(
    val success: Boolean,
    val data: List<OnlineTenantData>,
    val timestamp: String
)

data class OnlineTenantData(
    val tenant: String,
    val onlineUsers: Int
)

// UI States
sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(
        val usersWithForms: List<UsersWithFormsResponse>,
        val topUsers: List<TopUsersByFormTypeResponse>,
        val formMetrics: List<FormMetricsResponse>,
        val onlineUsers: List<OnlineUsersResponse>,
        val totalOnline: Int
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}