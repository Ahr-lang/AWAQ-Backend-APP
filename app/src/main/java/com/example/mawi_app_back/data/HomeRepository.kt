package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.presentation.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepository(private val apiService: AuthApiService) {

    suspend fun getHomeData(): HomeUiState.Success {
        return coroutineScope {
            // Load data for all tenants in parallel
            val tenants = listOf("agromo", "biomo", "roboranger")

            val topUsersDeferred = tenants.map { tenant ->
                async {
                    try {
                        val response = apiService.getTopUsersByFormType(tenant)
                        if (response.isSuccessful) {
                            response.body()?.let { topUsers ->
                                TopUsersByFormTypeResponse(tenant, topUsers)
                            }
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            val formMetricsDeferred = tenants.map { tenant ->
                async {
                    try {
                        val response = apiService.getFormMetrics(tenant)
                        if (response.isSuccessful) {
                            response.body()?.let { metrics ->
                                FormMetricsResponse(tenant, metrics)
                            }
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            val onlineUsersDeferred = tenants.map { tenant ->
                async {
                    try {
                        val response = apiService.getOnlineUsers(tenant)
                        if (response.isSuccessful) {
                            response.body()
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            // Get total online users
            val totalOnlineDeferred = async {
                try {
                    val response = apiService.getTotalOnlineUsers()
                    if (response.isSuccessful) {
                        response.body()?.total ?: 0
                    } else 0
                } catch (e: Exception) {
                    0
                }
            }

            // Wait for all to complete and get results
            val topUsers = topUsersDeferred.mapNotNull { it.await() }.ifEmpty {
                // Provide default empty data if no data returned
                tenants.map { tenant ->
                    TopUsersByFormTypeResponse(tenant, emptyList())
                }
            }
            val formMetrics = formMetricsDeferred.mapNotNull { it.await() }
            val onlineUsers = onlineUsersDeferred.mapNotNull { it.await() }.ifEmpty {
                // Provide default empty data if no data returned
                tenants.map { tenant ->
                    OnlineUsersResponse(tenant, 0, emptyList())
                }
            }
            val totalOnline = totalOnlineDeferred.await()

            HomeUiState.Success(
                topUsers = topUsers,
                formMetrics = formMetrics,
                onlineUsers = onlineUsers,
                totalOnline = totalOnline
            )
        }
    }
}