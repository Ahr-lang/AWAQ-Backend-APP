package com.example.mawi_app_back.presentation

import com.example.mawi_app_back.data.remote.models.UserDto

// Data models for HomeScreen API responses

data class UserFormsCount(
    val userId: Int,
    val username: String,
    val email: String,
    val formCount: Int
)

data class TopUserByFormType(
    val formType: String,
    val user: UserDto,
    val formCount: Int
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
    val tenant: String,
    val count: Int,
    val users: List<OnlineUser>
)

data class TotalOnlineUsersResponse(
    val total: Int,
    val byTenant: Map<String, Int>
)

// UI States
sealed class HomeUiState {
    object Idle : HomeUiState()
    object Loading : HomeUiState()
    data class Success(
        val topUsers: List<TopUsersByFormTypeResponse>,
        val formMetrics: List<FormMetricsResponse>,
        val onlineUsers: List<OnlineUsersResponse>,
        val totalOnline: Int
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}