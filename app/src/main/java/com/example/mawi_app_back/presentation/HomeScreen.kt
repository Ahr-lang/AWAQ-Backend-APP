package com.example.mawi_app_back.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mawi_app_back.presentation.components.*
import com.example.mawi_app_back.ui.theme.AwaqGreen
import com.example.mawi_app_back.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onLogout: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.loadData()
        }
    }

    AwaqBackground {
        AwaqCard(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                AwaqHeader(
                    title = "Dashboard Administrativo",
                    subtitle = "Monitorea la actividad de tus aplicaciones"
                )

                when (uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator(color = AwaqGreen)
                    }
                    is HomeUiState.Error -> {
                        ErrorMessage(
                            message = (uiState as HomeUiState.Error).message,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    is HomeUiState.Success -> {
                        val data = uiState as HomeUiState.Success

                        // Sección 1: Top Usuarios por Tipo de Formulario
                        TopUsersSection(data.topUsers)

                        Spacer(Modifier.height(16.dp))

                        // Sección 2: Métricas de Formularios
                        FormMetricsSection(data.formMetrics)

                        Spacer(Modifier.height(16.dp))

                        // Sección 3: Usuarios en Línea
                        OnlineUsersSection(data.onlineUsers, data.totalOnline)
                    }
                    HomeUiState.Idle -> {
                        // Nothing to show initially
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Botón de Cerrar Sesión
                TextButton(onClick = onLogout) {
                    Text(
                        "Cerrar sesión",
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Overlay de carga
        LoadingOverlay(isVisible = uiState is HomeUiState.Loading)
    }
}

@Composable
fun TopUsersSection(topUsers: List<TopUsersByFormTypeResponse>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Top Usuarios por Tipo de Formulario",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = AwaqGreen
        )
        Spacer(Modifier.height(8.dp))

        topUsers.forEach { tenantData ->
            Text(
                text = "Tenant: ${tenantData.tenant}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))

            tenantData.topUsers.forEach { topUser ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${topUser.formType}: ${topUser.user.username} (${topUser.formCount})")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun FormMetricsSection(formMetrics: List<FormMetricsResponse>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Métricas de Formularios",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = AwaqGreen
        )
        Spacer(Modifier.height(8.dp))

        formMetrics.forEach { tenantData ->
            Text(
                text = "Tenant: ${tenantData.tenant}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))

            tenantData.metrics.forEach { metric ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${metric.formType}: ${metric.count}")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun OnlineUsersSection(onlineUsers: List<OnlineUsersResponse>, totalOnline: Int) {
    AwaqCard {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Header
            Text(
                text = "Usuarios en Línea",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = AwaqGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // Total global
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AwaqGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Total Users: $totalOnline",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AwaqGreen,
                    modifier = Modifier.padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Spacer(Modifier.height(12.dp))

            // Separator
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            Spacer(Modifier.height(12.dp))

            // Lista por tenant
            onlineUsers.forEach { tenantData ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${tenantData.tenant.replaceFirstChar { it.uppercase() }} Users:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color(0xFF333333)
                    )
                    Surface(
                        color = AwaqGreen,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${tenantData.count}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}