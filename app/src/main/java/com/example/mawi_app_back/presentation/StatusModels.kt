package com.example.mawi_app_back.presentation

data class StatusResponse(
    val success: Boolean,
    val message: String?,
    val data: StatusPayload?,
    val timestamp: String?
)

data class StatusPayload(
    val period: String,            // "24h"
    val data: List<StatusPoint>    // puntos por hora (0..23)
)

data class StatusPoint(
    val hour: Int,                 // 0..23
    val timestamp: Long,           // epoch ms
    val requests: Int,
    val errors: Int,
    val errorRate: Double,         // porcentaje (ej. 0.96 => 0.96%)
    val status: String             // "green" | "yellow" | "red" (si lo manda el backend)
)

/* Opcional: /errors */
data class ErrorsResponse(
    val success: Boolean,
    val message: String?,
    val data: ErrorsPayload?,
    val timestamp: String?
)

data class ErrorsPayload(
    val httpErrors: List<HttpErrorMetric>?,
    val totalErrors: TotalErrors?,
    val applicationErrors: List<AppErrorMetric>?
)

data class HttpErrorMetric(
    val tenant: String,
    val status: String,
    val errorRate: Double
)

data class TotalErrors(
    val totalErrorRate: Double
)

data class AppErrorMetric(
    val tenant: String,
    val applicationErrors: Double
)