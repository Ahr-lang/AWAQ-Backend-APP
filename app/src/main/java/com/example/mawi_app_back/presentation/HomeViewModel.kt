package com.example.mawi_app_back.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mawi_app_back.data.remote.AuthApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val apiService: AuthApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val tenants = listOf("agromo", "biomo", "roboranger")

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                // Load data for all tenants in parallel
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
                val onlineUsers = onlineUsersDeferred.mapIndexedNotNull { index, deferred ->
                    val body = deferred.await()
                    if (body != null) {
                        OnlineUsersResponse(
                            tenant = tenants[index],
                            count = body.count,
                            users = body.users ?: emptyList()
                        )
                    } else null
                }.ifEmpty {
                    // Provide default empty data if no data returned
                    tenants.map { tenant ->
                        OnlineUsersResponse(tenant, 0, emptyList())
                    }
                }
                val totalOnline = totalOnlineDeferred.await()

                _uiState.value = HomeUiState.Success(
                    topUsers = topUsers,
                    formMetrics = formMetrics,
                    onlineUsers = onlineUsers,
                    totalOnline = totalOnline
                )

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al cargar datos: ${e.localizedMessage ?: "Error desconocido"}")
            }
        }
    }

    fun refresh() {
        loadData()
    }
}