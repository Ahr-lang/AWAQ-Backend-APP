package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.presentation.StatusPoint

class StatusRepository(
    private val api: AuthApiService
) {
    suspend fun getTenantStatus(tenant: String): List<StatusPoint> {
        val resp = api.getAdminStatus(tenant)
        if (resp.isSuccessful) {
            val list = resp.body()?.data?.data ?: emptyList()
            // Normaliza a 24 horas; si falta alguna, la marcamos como sin actividad (rojo).
            val byHour = list.associateBy { it.hour }
            return (0..23).map { h ->
                byHour[h] ?: StatusPoint(
                    hour = h, timestamp = 0L, requests = 0, errors = 0,
                    errorRate = 0.0, status = "red"
                )
            }
        }
        error("Error ${resp.code()}: ${resp.message()}")
    }
}