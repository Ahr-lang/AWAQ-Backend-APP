package com.example.mawi_app_back.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.presentation.StatusPoint
import com.example.mawi_app_back.domain.usecase.TenantStatusRow
import com.example.mawi_app_back.presentation.ErrorItem
import com.example.mawi_app_back.presentation.components.*
import com.example.mawi_app_back.ui.theme.AwaqGreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore

@Composable
fun StatusScreen(viewModel: StatusViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val Ink = Color(0xFF111111)
    val Subtle = Color(0xFF50565A)
    val Yellow = Color(0xFFF59E0B)
    val Red = Color(0xFFDC2626)
    val DividerSoft = Color(0xFFE6E8EA)

    var legendVisible by remember { mutableStateOf(true) }
    var expandedApp by remember { mutableStateOf<String?>(null) }
    var showErrorsDialog by remember { mutableStateOf(false) }
    var selectedErrors by remember { mutableStateOf<List<ErrorItem>>(emptyList()) }

    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(containerColor = Color.Transparent) { inner ->
        AwaqBackground(modifier = Modifier.padding(inner)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título principal
                Text(
                    text = "Status del Sistema",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = AwaqGreen,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                when (val s = uiState) {
                    StatusUiState.Loading, StatusUiState.Idle -> {
                        CircularProgressIndicator(color = AwaqGreen)
                    }
                    is StatusUiState.Error -> {
                        ErrorMessage(message = s.message)
                    }
                    is StatusUiState.Success -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Orden específico: agromo, biomo, robo
                            val orderedApps = listOf("agromo", "biomo", "robo")
                            
                            orderedApps.forEach { appName ->
                                val row = s.rows.find { it.tenant.equals(appName, ignoreCase = true) }
                                row?.let {
                                    AppStatusCard(
                                        row = it,
                                        isExpanded = expandedApp == it.tenant,
                                        onCardClick = { expandedApp = if (expandedApp == it.tenant) null else it.tenant },
                                        onErrorClick = { error -> 
                                            selectedErrors = listOf(error)
                                            showErrorsDialog = true 
                                        },
                                        green = AwaqGreen,
                                        yellow = Yellow,
                                        red = Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            LoadingOverlay(isVisible = uiState is StatusUiState.Loading)
        }
    }

    // Diálogo de detalle de error
    if (showErrorsDialog && selectedErrors.isNotEmpty()) {
        ErrorDetailDialog(
            error = selectedErrors.first(),
            onDismiss = { showErrorsDialog = false }
        )
    }
}

@Composable
fun AppStatusCard(
    row: TenantStatusRow,
    isExpanded: Boolean,
    onCardClick: () -> Unit,
    onErrorClick: (ErrorItem) -> Unit,
    green: Color,
    yellow: Color,
    red: Color
) {
    val appName = row.tenant.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    
    // Determinar el color del status y el texto del estado
    val statusColor = when (row.dailyStatus) {
        "green" -> green
        "yellow" -> yellow
        else -> red
    }
    
    val statusText = when (row.dailyStatus) {
        "green" -> "OK"
        "yellow" -> "Degradado"
        else -> "Crítico"
    }
    
    // Calcular el total de errores en 24 horas
    val totalErrors = row.hours.sumOf { it.errors }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) statusColor.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header de la tarjeta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Indicador tipo semáforo (círculo)
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusColor)
                    )
                    
                    Text(
                        text = appName.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = statusColor
                    )
                    
                    Text(
                        text = "|",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF999999)
                    )
                    
                    Text(
                        text = "$totalErrors errores",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF666666)
                    )
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = statusColor
                )
            }

            // Contenido expandido
            if (isExpanded) {
                Spacer(Modifier.height(16.dp))
                
                // Estado actual de la app
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Estado: $statusText",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = statusColor,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                
                Spacer(Modifier.height(12.dp))
                
                // Listado de errores recientes
                Text(
                    text = "Errores Recientes",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (row.errors.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "No hay errores registrados",
                            fontSize = 12.sp,
                            color = Color(0xFF999999),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    // Mostrar hasta 5 errores más recientes
                    row.errors.take(5).forEach { error ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable { onErrorClick(error) },
                            color = Color(0xFFF8F9FA),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = error.operation,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = Color(0xFF333333)
                                    )
                                    Text(
                                        text = error.message,
                                        fontSize = 11.sp,
                                        color = Color(0xFF666666),
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                                    contentDescription = "Ver detalle",
                                    tint = Color(0xFF999999),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorDetailDialog(
    error: ErrorItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Detalle del Error",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ) 
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Error ID Card
                InfoCard(title = "ID del Error") {
                    Text(
                        text = error.id,
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        fontFamily = FontFamily.Monospace
                    )
                }
                
                // Request Information
                InfoCard(title = "Información de la Solicitud") {
                    DetailRow("Operación", error.operation)
                    error.method?.let { DetailRow("Método", it) }
                    error.url?.let { DetailRow("URL", it) }
                    error.statusCode?.let { 
                        DetailRow(
                            "Status Code", 
                            it.toString(),
                            valueColor = if (it >= 500) Color(0xFFDC2626) else Color(0xFFEA580C)
                        )
                    }
                }
                
                // Error Details
                InfoCard(title = "Detalles del Error") {
                    DetailRow("Tipo", error.errorType.uppercase())
                    DetailRow("Mensaje", error.message)
                    DetailRow("Tenant", error.tenant.uppercase())
                }
                
                // Timestamp
                InfoCard(title = "Fecha y Hora") {
                    val formattedTime = try {
                        val instant = java.time.Instant.parse(error.timestamp)
                        val formatter = java.time.format.DateTimeFormatter
                            .ofPattern("dd/MM/yyyy HH:mm:ss")
                            .withZone(java.time.ZoneId.of("UTC"))
                        formatter.format(instant) + " UTC"
                    } catch (e: Exception) {
                        error.timestamp
                    }
                    DetailRow("Timestamp", formattedTime)
                }
                
                // Context Information
                error.context?.let { context ->
                    if (context.isNotEmpty()) {
                        InfoCard(title = "Contexto Adicional") {
                            context.forEach { (key, value) ->
                                if (value !is Map<*, *>) {
                                    DetailRow(
                                        key.replaceFirstChar { it.uppercase() },
                                        value.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AwaqGreen
                )
            ) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun InfoCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8F9FA),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = AwaqGreen
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE5E7EB)
            )
            content()
        }
    }
}

@Composable
fun DetailRow(
    label: String, 
    value: String,
    valueColor: Color = Color(0xFF111827),
    maxLines: Int = Int.MAX_VALUE
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = valueColor,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun AppStatusPanel(
    row: TenantStatusRow,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onHourClick: (List<ErrorItem>) -> Unit,
    ink: Color,
    green: Color,
    yellow: Color,
    red: Color
) {
    val appName = row.tenant.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(vertical = 8.dp)
        ) {
            Text(appName, fontWeight = FontWeight.SemiBold, color = ink, modifier = Modifier.weight(1f))
            val chipColor = when (row.dailyStatus) { "green" -> green; "yellow" -> yellow; else -> red }
            Surface(color = chipColor.copy(alpha = .12f), shape = RoundedCornerShape(999.dp)) {
                Text(row.dailyStatus.uppercase(), color = chipColor, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Contraer" else "Expandir"
            )
        }
        if (isExpanded) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items<StatusPoint>(row.hours) { point ->
                    HourDetailRow(point, green, yellow, red, ink, onHourClick, row.errors, row.tenant)
                }
            }
        }
    }
}

@Composable
private fun HourDetailRow(point: StatusPoint, green: Color, yellow: Color, red: Color, ink: Color, onHourClick: (List<ErrorItem>) -> Unit, allErrors: List<ErrorItem>, tenant: String) {
    val color = when (point.status) {
        "green" -> green
        "yellow" -> yellow
        "red" -> red
        else -> green
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Filter errors by tenant
                val tenantErrors = allErrors.filter { it.tenant == tenant }
                onHourClick(tenantErrors)
            }
    ) {
        Text("${point.hour}:00", color = ink, fontSize = 12.sp, modifier = Modifier.width(40.dp))
        Box(
            modifier = Modifier
                .size(width = 20.dp, height = 12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text("${point.errors} errores", color = ink, fontSize = 12.sp)
    }
}

@Composable
private fun HourBlock(point: StatusPoint, green: Color, yellow: Color, red: Color) {
    val color = when (point.status) {
        "green" -> green
        "yellow" -> yellow
        "red" -> red
        else -> green
    }
    Box(
        modifier = Modifier
            .size(width = 12.dp, height = 28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}

@Composable
private fun LegendDot(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
    )
}
