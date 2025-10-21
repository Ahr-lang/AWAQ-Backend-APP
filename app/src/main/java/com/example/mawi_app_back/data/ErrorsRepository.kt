package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.presentation.ErrorItem

class ErrorsRepository(
    private val api: AuthApiService
) {
    suspend fun getTenantErrors(tenant: String): List<ErrorItem> {
        val resp = api.getAdminErrors(tenant)
        if (resp.isSuccessful) {
            return resp.body()?.data ?: emptyList()
        }
        error("Error ${resp.code()}: ${resp.message()}")
    }
}