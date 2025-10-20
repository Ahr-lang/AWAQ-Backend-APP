package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.StatusRepository
import com.example.mawi_app_back.presentation.StatusPoint

data class TenantStatusRow(
    val tenant: String,
    val hours: List<StatusPoint>,
    val dailyStatus: String
)

class GetStatusDashboardUseCase(
    private val repo: StatusRepository
) {
    private val tenants = listOf("agromo", "biomo", "robo")

    suspend operator fun invoke(): List<TenantStatusRow> {
        return tenants.map { tenant ->
            val hours = repo.getTenantStatus(tenant)
            val daily = computeDailyStatus(hours)
            TenantStatusRow(tenant = tenant, hours = hours, dailyStatus = daily)
        }
    }

    private fun computeDailyStatus(hours: List<StatusPoint>): String {
        // rojo si alguna hora >5% o sin actividad
        if (hours.any { it.requests == 0 || it.errorRate > 5.0 || it.status.equals("red", true) })
            return "red"
        // amarillo si alguna hora 1..5%
        if (hours.any { it.errorRate in 1.0..5.0 || it.status.equals("yellow", true) })
            return "yellow"
        return "green"
    }
}