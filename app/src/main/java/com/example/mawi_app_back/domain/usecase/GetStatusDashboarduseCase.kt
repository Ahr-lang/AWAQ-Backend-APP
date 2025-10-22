package com.example.mawi_app_back.domain.usecase

import com.example.mawi_app_back.data.StatusRepository
import com.example.mawi_app_back.presentation.StatusPoint
import com.example.mawi_app_back.presentation.ErrorItem

data class TenantStatusRow(
    val tenant: String,
    val hours: List<StatusPoint>,
    val dailyStatus: String,
    val errors: List<ErrorItem>
)

class GetStatusDashboardUseCase(
    private val repo: StatusRepository
) {
    private val tenants = listOf("agromo", "biomo", "robo")

    suspend operator fun invoke(): List<TenantStatusRow> {
        return tenants.map { tenant ->
            val hours = repo.getTenantStatus(tenant)
            val daily = computeDailyStatus(hours)
            val errors = repo.getTenantErrors(tenant).flatMap { it.recentErrors }
            TenantStatusRow(tenant = tenant, hours = hours, dailyStatus = daily, errors = errors)
        }
    }

    private fun computeDailyStatus(hours: List<StatusPoint>): String {
        if (hours.any { it.status == "red" }) return "red"
        if (hours.any { it.status == "yellow" }) return "yellow"
        return "green"
    }
}