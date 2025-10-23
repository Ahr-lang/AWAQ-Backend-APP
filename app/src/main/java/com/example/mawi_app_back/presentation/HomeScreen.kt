package com.example.mawi_app_back.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
    viewModel: HomeViewModel,
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
                // Header - Título Grande
                Text(
                    text = "Formularios Total",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = AwaqGreen,
                    modifier = Modifier.padding(vertical = 16.dp)
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

                        // Tarjetas clickeables de aplicaciones
                        ApplicationCardsSection(
                            topUsers = data.topUsers,
                            formMetrics = data.formMetrics
                        )

                        Spacer(Modifier.height(24.dp))

                        // Separador visual
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = AwaqGreen.copy(alpha = 0.3f),
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )

                        Spacer(Modifier.height(24.dp))

                        // Sección de Usuarios en Línea (sin cambios)
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
fun ApplicationCardsSection(
    topUsers: List<TopUsersByFormTypeResponse>,
    formMetrics: List<FormMetricsResponse>
) {
    var expandedApp by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Filtrar solo las apps principales
        val apps = listOf("biomo", "agromo", "robo")
        
        apps.forEach { appName ->
            ApplicationCard(
                appName = appName,
                isExpanded = expandedApp == appName,
                onCardClick = { expandedApp = if (expandedApp == appName) null else appName },
                topUsers = topUsers.find { it.tenant.equals(appName, ignoreCase = true) },
                formMetrics = formMetrics.find { it.tenant.equals(appName, ignoreCase = true) }
            )
        }
    }
}

@Composable
fun ApplicationCard(
    appName: String,
    isExpanded: Boolean,
    onCardClick: () -> Unit,
    topUsers: TopUsersByFormTypeResponse?,
    formMetrics: FormMetricsResponse?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) AwaqGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
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
                // Calcular el total de formularios
                val totalForms = formMetrics?.metrics?.sumOf { it.count } ?: 0
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = appName.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AwaqGreen
                    )
                    Text(
                        text = "|",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF999999)
                    )
                    Text(
                        text = "$totalForms",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF666666)
                    )
                }
                
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.ExpandLess 
                    else 
                        Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = AwaqGreen
                )
            }

            // Contenido expandido
            if (isExpanded) {
                Spacer(Modifier.height(16.dp))
                
                var showUsersForms by remember { mutableStateOf(false) }
                var showFormTypes by remember { mutableStateOf(false) }
                
                // Sección desplegable: Formularios por Usuario
                ExpandableSection(
                    title = "Formularios por Usuario",
                    isExpanded = showUsersForms,
                    onToggle = { showUsersForms = !showUsersForms }
                ) {
                    if (topUsers?.topUsers.isNullOrEmpty()) {
                        Text(
                            text = "Sin datos",
                            fontSize = 12.sp,
                            color = Color(0xFF999999)
                        )
                    } else {
                        topUsers?.topUsers?.forEach { user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = user.user.username,
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333),
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    color = AwaqGreen,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${user.formCount}",
                                        fontSize = 11.sp,
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                // Sección desplegable: Total por Tipo
                ExpandableSection(
                    title = "Total por Tipo",
                    isExpanded = showFormTypes,
                    onToggle = { showFormTypes = !showFormTypes }
                ) {
                    if (formMetrics?.metrics.isNullOrEmpty()) {
                        Text(
                            text = "Sin datos",
                            fontSize = 12.sp,
                            color = Color(0xFF999999)
                        )
                    } else {
                        formMetrics?.metrics?.forEach { metric ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = metric.formType.replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333),
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    color = AwaqGreen.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${metric.count}",
                                        fontSize = 11.sp,
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ExpandableSection(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8F9FA),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header clickeable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = AwaqGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Contenido expandible
            if (isExpanded) {
                Spacer(Modifier.height(8.dp))
                content()
            }
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
                        text = "${tenantData.tenant?.replaceFirstChar { it.uppercase() } ?: "Unknown"} Users:",
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