package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.presentation.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepository(private val apiService: AuthApiService) {

    suspend fun getHomeData(): HomeUiState.Success {
        return coroutineScope {
            // Load data for all tenants in parallel
            val tenants = listOf("agromo", "biomo", "back")

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

            val onlineUsersDeferred = async {
                try {
                    val response = apiService.getOnlineUsers("agromo")  // Use a valid tenant like agromo
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            if (apiResponse.success) {
                                // Convert to list of OnlineUsersResponse
                                apiResponse.data.map { tenantData ->
                                    OnlineUsersResponse(
                                        tenant = tenantData.tenant,
                                        count = tenantData.onlineUsers,
                                        users = emptyList() // Swagger doesn't provide user list
                                    )
                                }
                            } else emptyList()
                        } ?: emptyList()
                    } else {
                        println("Error fetching online users: ${response.code()} - ${response.message()}")
                        emptyList()
                    }
                } catch (e: Exception) {
                    println("Exception fetching online users: ${e.message}")
                    emptyList()
                }
            }

            // Get total online users (calculate from individual tenant data)
            val totalOnlineDeferred = async {
                // Calculate from individual tenant data after collecting it
                0 // Placeholder, will be updated below
            }

            // Wait for all to complete and get results
            val topUsers = topUsersDeferred.mapNotNull { it.await() }.ifEmpty {
                // Provide default empty data if no data returned
                tenants.map { tenant ->
                    TopUsersByFormTypeResponse(tenant, emptyList())
                }
            }
            val formMetrics = formMetricsDeferred.await()
            val onlineUsers = onlineUsersDeferred.await()
            val totalOnlineFromGlobal = totalOnlineDeferred.await()
            // Always calculate from individual tenant data since global endpoint doesn't exist
            val totalOnline = onlineUsers.sumOf { it.count }

            HomeUiState.Success(
                topUsers = topUsers,
                formMetrics = formMetrics,
                onlineUsers = onlineUsers,
                totalOnline = totalOnline
            )
        }
    }
}