package com.example.mawi_app_back.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.presentation.StatusPoint
import com.example.mawi_app_back.domain.usecase.TenantStatusRow
import com.example.mawi_app_back.presentation.ErrorItem
import com.example.mawi_app_back.presentation.components.*
import com.example.mawi_app_back.ui.theme.AwaqGreen
import androidx.compose.material.icons.Icons
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Estado del sistema", fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(6.dp))
                Text("Últimas 24 horas por tenant", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)

                Spacer(Modifier.height(16.dp))

                AwaqSimpleCard {
                    // Toggleable Leyenda
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Leyenda", fontWeight = FontWeight.SemiBold, color = Ink)
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { legendVisible = !legendVisible }) {
                            Icon(
                                imageVector = if (legendVisible) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (legendVisible) "Ocultar leyenda" else "Mostrar leyenda"
                            )
                        }
                    }
                    if (legendVisible) {
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LegendDot(AwaqGreen); Text("  Correcto", color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Yellow);   Text("  Parcial", color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Red);      Text("  Crítico", color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = DividerSoft)
                    Spacer(Modifier.height(12.dp))

                    when (val s = uiState) {
                        StatusUiState.Loading, StatusUiState.Idle -> {
                            LoadingIndicator(
                                modifier = Modifier.height(180.dp)
                            )
                        }
                        is StatusUiState.Error -> {
                            ErrorMessage(message = s.message)
                        }
                        is StatusUiState.Success -> {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                s.rows.forEach { row ->
                                    AppStatusPanel(row, expandedApp == row.tenant, { expandedApp = if (expandedApp == row.tenant) null else row.tenant }, { errors -> selectedErrors = errors; showErrorsDialog = true }, MaterialTheme.colorScheme.onBackground, AwaqGreen, Yellow, Red)
                                }
                            }
                        }
                    }
                }
            }

            LoadingOverlay(isVisible = uiState is StatusUiState.Loading)
        }
    }

    if (showErrorsDialog) {
        AlertDialog(
            onDismissRequest = { showErrorsDialog = false },
            title = { Text("Errores de la Hora") },
            text = {
                LazyColumn {
                    if (selectedErrors.isEmpty()) {
                        item {
                            Text("No hay errores en esta hora", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                        }
                    } else {
                        items(selectedErrors) { error ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = error.operation,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Mensaje: ${error.message}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                error.statusCode?.let {
                                    Text(
                                        text = "Status: $it",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "Hora: ${error.timestamp}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                error.userId?.let {
                                    Text(
                                        text = "User ID: $it",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showErrorsDialog = false }) {
                    Text("Cerrar")
                }
            }
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
