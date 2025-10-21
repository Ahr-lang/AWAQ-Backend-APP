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
                            response.body()?.let { topUsersResponse ->
                                // Safely access topUsers property
                                val topUsers = topUsersResponse.topUsers ?: emptyList()
                                TopUsersByFormTypeResponse(tenant, topUsers)
                            }
                        } else null
                    } catch (e: Exception) {
                        // Return null if anything fails
                        null
                    }
                }
            }

            val formMetricsDeferred = async {
                try {
                    val response = apiService.getAllFormMetrics()
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            // Group by tenant
                            val grouped = apiResponse.data.groupBy { it.tenant }.map { (tenant, items) ->
                                val metrics = items.map { FormMetrics(it.form_type, it.count) }
                                FormMetricsResponse(tenant, metrics)
                            }
                            grouped
                        } ?: emptyList()
                    } else {
                        // If endpoint doesn't exist, try alternative approach or return empty
                        emptyList()
                    }
                } catch (e: Exception) {
                    // If endpoint fails, return empty list instead of crashing
                    emptyList()
                }
            }

            val onlineUsersDeferred = tenants.map { tenant ->
                async {
                    try {
                        val response = apiService.getOnlineUsers(tenant)
                        if (response.isSuccessful) {
                            response.body()?.let { onlineUsersResponse ->
                                // Ensure we have valid data
                                OnlineUsersResponse(
                                    tenant = tenant,
                                    count = onlineUsersResponse.count,
                                    users = onlineUsersResponse.users ?: emptyList()
                                )
                            }
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
                    } else {
                        // If endpoint doesn't exist, calculate from individual tenant data
                        0 // Will be updated after collecting individual data
                    }
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
            val formMetrics = formMetricsDeferred.await()
            val onlineUsers = onlineUsersDeferred.mapNotNull { it.await() }.ifEmpty {
                // Provide default empty data if no data returned
                tenants.map { tenant ->
                    OnlineUsersResponse(tenant, 0, emptyList())
                }
            }
            val totalOnlineFromGlobal = totalOnlineDeferred.await()
            // If global endpoint failed (returned 0), calculate from individual tenant data
            val totalOnline = if (totalOnlineFromGlobal > 0) {
                totalOnlineFromGlobal
            } else {
                onlineUsers.sumOf { it.count }
            }

            HomeUiState.Success(
                topUsers = topUsers,
                formMetrics = formMetrics,
                onlineUsers = onlineUsers,
                totalOnline = totalOnline
            )
        }
    }
}