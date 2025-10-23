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
            val allErrors = repo.getTenantErrors(tenant).flatMap { it.recentErrors }
            // Filter errors to only show those matching the current tenant
            val errors = allErrors.filter { it.tenant.equals(tenant, ignoreCase = true) }
            TenantStatusRow(tenant = tenant, hours = hours, dailyStatus = daily, errors = errors)
        }
    }

    private fun computeDailyStatus(hours: List<StatusPoint>): String {
        val hasGreen = hours.any { it.status == "green" }
        val hasRed = hours.any { it.status == "red" }
        val hasYellow = hours.any { it.status == "yellow" }
        
        // If there's a mix of green and red (inconsistent), show yellow (warning)
        if (hasGreen && hasRed) return "yellow"
        
        // If all are green, show green (good)
        if (hasGreen && !hasRed && !hasYellow) return "green"
        
        // If all are red, show red (bad)
        if (hasRed && !hasGreen) return "red"
        
        // If there's any yellow, show yellow
        if (hasYellow) return "yellow"
        
        return "green"
    }
}