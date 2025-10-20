package com.example.mawi_app_back.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.presentation.StatusPoint
import com.example.mawi_app_back.domain.usecase.TenantStatusRow
import com.example.mawi_app_back.ui.theme.AwaqGreen

@Composable
fun StatusScreen(viewModel: StatusViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val Ink = Color(0xFF111111)
    val Subtle = Color(0xFF50565A)
    val GreenSoftBg = AwaqGreen.copy(alpha = 0.08f)
    val Yellow = Color(0xFFF59E0B)
    val Red = Color(0xFFDC2626)
    val DividerSoft = Color(0xFFE6E8EA)

    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(containerColor = Color.Transparent) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(GreenSoftBg, Color.White)))
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Estado del sistema", fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Ink)
                Spacer(Modifier.height(6.dp))
                Text("Últimas 24 horas por tenant", color = Subtle, fontSize = 14.sp)

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        // Leyenda
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LegendDot(AwaqGreen); Text("  < 1% errores", color = Subtle)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Yellow);   Text("  1–5% errores", color = Subtle)
                            Spacer(Modifier.width(16.dp))
                            LegendDot(Red);      Text("  >5% o sin actividad", color = Subtle)
                        }
                        Spacer(Modifier.height(12.dp))
                        Divider(color = DividerSoft)
                        Spacer(Modifier.height(12.dp))

                        when (val s = uiState) {
                            StatusUiState.Loading, StatusUiState.Idle -> {
                                Box(Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = AwaqGreen)
                                }
                            }
                            is StatusUiState.Error -> {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) { Text(s.message, Modifier.padding(12.dp), fontSize = 13.sp) }
                            }
                            is StatusUiState.Success -> {
                                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                    s.rows.forEach { row ->
                                        TenantRow(row, Ink, AwaqGreen, Yellow, Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = uiState is StatusUiState.Loading, enter = fadeIn(), exit = fadeOut()) {
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.08f)))
            }
        }
    }
}

@Composable
private fun TenantRow(
    row: TenantStatusRow,
    ink: Color,
    green: Color,
    yellow: Color,
    red: Color
) {
    val title = row.tenant.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(title, fontWeight = FontWeight.SemiBold, color = ink, modifier = Modifier.weight(1f))
            val chipColor = when (row.dailyStatus) { "green" -> green; "yellow" -> yellow; else -> red }
            Surface(color = chipColor.copy(alpha = .12f), shape = RoundedCornerShape(999.dp)) {
                Text(row.dailyStatus.uppercase(), color = chipColor, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(row.hours) { p -> HourBlock(p, green, yellow, red) }
        }
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
