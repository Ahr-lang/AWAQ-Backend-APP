package com.example.mawi_app_back.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TenantManager(
    defaultTenant: String = "back"
) {
    private val _tenant = MutableStateFlow(defaultTenant)
    val tenant = _tenant.asStateFlow()

    private val tenantApiKeys = mapOf(
        "back" to "back-key-000",
        "agromo" to "agromo-key-123",
        "biomo" to "biomo-key-456",
        "robo" to "robo-key-789"
    )

    fun setTenant(newTenant: String) { _tenant.value = newTenant }

    fun currentApiKey(): String =
        tenantApiKeys[tenant.value] ?: tenantApiKeys.getValue("back")
}
