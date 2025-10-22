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

    private val tenants = listOf("agromo", "biomo", "robo")

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                // Load data for all tenants in parallel
                val usersWithFormsDeferred = tenants.map { tenant ->
                    async {
                        try {
                            val response = apiService.getUsersWithFormCounts(tenant)
                            if (response.isSuccessful) {
                                response.body()?.let { apiResponse ->
                                    UsersWithFormsResponse(apiResponse.tenant, apiResponse.data)
                                }
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                val topUsersDeferred = tenants.map { tenant ->
                    async {
                        try {
                            val response = apiService.getTopUsersByFormType(tenant)
                            if (response.isSuccessful) {
                                response.body()?.let { apiResponse ->
                                    TopUsersByFormTypeResponse(apiResponse.tenant, apiResponse.data)
                                }
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                // Get top users data first
                val topUsersData = topUsersDeferred.map { it.await() }.filterNotNull()

                // Derive form metrics from top users data
                val formMetricsData = topUsersData.map { topUserResponse ->
                    val metrics = topUserResponse.topUsers.map { topUser ->
                        FormMetrics(
                            formType = topUser.formType,
                            count = topUser.formCount
                        )
                    }
                    FormMetricsResponse(
                        tenant = topUserResponse.tenant,
                        metrics = metrics
                    )
                }

                val onlineUsersDeferred = async {
                    try {
                        val response = apiService.getOnlineUsers("agromo")
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
                            emptyList()
                        }
                    } catch (e: Exception) {
                        emptyList()
                    }
                }

                // Calculate total online users from the fetched data

                // Wait for all to complete and get results
                val usersWithFormsData = usersWithFormsDeferred.map { it.await() }.filterNotNull()
                val onlineUsers = onlineUsersDeferred.await()
                val totalOnline = onlineUsers.sumOf { it.count }

                _uiState.value = HomeUiState.Success(
                    usersWithForms = usersWithFormsData,
                    topUsers = topUsersData,
                    formMetrics = formMetricsData,
                    onlineUsers = onlineUsers,
                    totalOnline = totalOnline
                )

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al cargar datos: ${e.localizedMessage ?: "Error desconocido"}")
            }
        }
    }
}