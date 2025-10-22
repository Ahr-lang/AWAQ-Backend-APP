package com.example.mawi_app_back.data

import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.presentation.StatusPoint
import com.example.mawi_app_back.presentation.HttpErrorGroup

class StatusRepository(
    private val api: AuthApiService
) {
    suspend fun getTenantStatus(tenant: String): List<StatusPoint> {
        val resp = api.getAdminStatus(tenant)
        if (resp.isSuccessful) {
            val list = resp.body()?.data?.data ?: emptyList()
            // Normaliza a 24 horas; si falta alguna, la marcamos como sin actividad (green).
            val byHour = list.associateBy { it.hour }
            return (0..23).map { h ->
                val sr = byHour[h]
                if (sr != null) {
                    val mappedStatus = when (sr.status) {
                        "0" -> "green"
                        "1" -> "yellow"
                        "2" -> "red"
                        else -> sr.status
                    }
                    StatusPoint(
                        hour = sr.hour,
                        timestamp = sr.timestamp,
                        requests = sr.requests,
                        errors = sr.errors,
                        errorRate = sr.errorRate,
                        status = if (sr.errors == 0) "green" else mappedStatus
                    )
                } else {
                    StatusPoint(
                        hour = h,
                        timestamp = 0L,
                        requests = 0,
                        errors = 0,
                        errorRate = 0.0,
                        status = "green"
                    )
                }
            }
        }
        error("Error ${resp.code()}: ${resp.message()}")
    }

    suspend fun getTenantErrors(tenant: String): List<HttpErrorGroup> {
        val resp = api.getAdminErrors(tenant)
        if (resp.isSuccessful) {
            return resp.body()?.data?.httpErrors ?: emptyList()
        }
        error("Error ${resp.code()}: ${resp.message()}")
    }
}