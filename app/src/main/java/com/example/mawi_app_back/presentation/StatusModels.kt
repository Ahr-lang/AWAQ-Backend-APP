package com.example.mawi_app_back.presentation

data class StatusResponse(
    val success: Boolean,
    val message: String?,
    val data: StatusPayload?,
    val timestamp: String?
)

data class StatusPayload(
    val period: String,            // "24h"
    val data: List<ApiStatusPoint>    // puntos por hora (0..23)
)

data class ApiStatusPoint(
    val hour: Int,                 // 0..23
    val timestamp: Long,           // epoch ms
    val requests: Int,
    val errors: Int,
    val errorRate: Double,         // porcentaje (ej. 0.96 => 0.96%)
    val status: String             // "green" | "yellow" | "red"
)

data class StatusPoint(
    val hour: Int,                 // 0..23
    val timestamp: Long,           // epoch ms
    val requests: Int,
    val errors: Int,
    val errorRate: Double,         // porcentaje (ej. 0.96 => 0.96%)
    val status: String             // "green" | "yellow" | "red"
)

/* Opcional: /errors */
data class ErrorsResponse(
    val success: Boolean,
    val message: String?,
    val data: ErrorsPayload?,
    val timestamp: String?
)

data class ErrorsPayload(
    val httpErrors: List<HttpErrorGroup>,
    val totalErrors: TotalErrors,
    val applicationErrors: List<Any>
)

data class HttpErrorGroup(
    val tenant: String,
    val status: String,
    val count: Int,
    val errorRate: Double,
    val recentErrors: List<ErrorItem>
)

data class TotalErrors(
    val totalErrors: Int,
    val totalErrorRate: Double,
    val timeRange: String,
    val errorsByType: Map<String, Int>,
    val errorsByTenant: Map<String, Int>
)

data class ErrorItem(
    val id: String,
    val timestamp: String,
    val tenant: String,
    val operation: String,
    val errorType: String,
    val message: String,
    val statusCode: Int?,
    val userId: String?,
    val userAgent: String?,
    val ip: String?,
    val method: String?,
    val url: String?,
    val context: Map<String, Any>?
)