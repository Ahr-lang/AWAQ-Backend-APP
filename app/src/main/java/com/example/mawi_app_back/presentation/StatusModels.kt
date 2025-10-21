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
    val tenant: String?,
    val data: List<ErrorItem>?,
    val count: Int?,
    val timestamp: String?
)

data class ErrorItem(
    val id: String,
    val timestamp: String,
    val level: String,
    val message: String,
    val user_id: Int?,
    val operation: String?,
    val details: Map<String, Any>?
)