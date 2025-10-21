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

    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(containerColor = Color.Transparent) { inner ->
        AwaqBackground(modifier = Modifier.padding(inner)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Estado del sistema", fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Ink)
                Spacer(Modifier.height(6.dp))
                Text("Últimas 24 horas por tenant", color = Subtle, fontSize = 14.sp)

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
                            LegendDot(AwaqGreen); Text("  No errores", color = Subtle)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Yellow);   Text("  Algunos errores", color = Subtle)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Red);      Text("  Solo errores", color = Subtle)
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
                                    AppStatusPanel(row, expandedApp == row.tenant, { expandedApp = if (expandedApp == row.tenant) null else row.tenant }, Ink, AwaqGreen, Yellow, Red)
                                }
                            }
                        }
                    }
                }
            }

            LoadingOverlay(isVisible = uiState is StatusUiState.Loading)
        }
    }
}

@Composable
private fun AppStatusPanel(
    row: TenantStatusRow,
    isExpanded: Boolean,
    onToggle: () -> Unit,
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
                Text(row.dailyStatus.uppercase(), color = chipColor, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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
                    HourDetailRow(point, green, yellow, red, ink)
                }
            }
        }
    }
}

@Composable
private fun HourDetailRow(point: StatusPoint, green: Color, yellow: Color, red: Color, ink: Color) {
    val color = when {
        point.requests == 0 -> red
        point.errorRate < 1.0 -> green
        point.errorRate < 5.0 -> yellow
        else -> red
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
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
    val color = when {
        point.requests == 0 -> red
        point.errorRate < 1.0 -> green
        point.errorRate < 5.0 -> yellow
        else -> red
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
