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

                        // Sección 1: Usuarios con Formularios
                        UsersWithFormsSection(data.usersWithForms)

                        Spacer(Modifier.height(16.dp))

                        // Sección 2: Cantidad de Formularios por Usuarios
                        TopUsersSection(data.topUsers)

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
fun UsersWithFormsSection(usersWithForms: List<UsersWithFormsResponse>) {
    AwaqCard {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Header
            Text(
                text = "Usuarios con Formularios",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = AwaqGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // Contenido
            usersWithForms.forEach { tenantData ->
                // Header del tenant
                Text(
                    text = tenantData.tenant.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (tenantData.users.isEmpty()) {
                    // No hay usuarios
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        color = Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Sin usuarios registrados",
                            fontSize = 14.sp,
                            color = Color(0xFF999999),
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    // Mostrar usuarios
                    tenantData.users.forEach { user ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
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
                                        text = user.username,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = Color(0xFF333333)
                                    )
                                    Text(
                                        text = user.user_email,
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666)
                                    )
                                }
                                Surface(
                                    color = AwaqGreen,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${user.forms_count}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TopUsersSection(topUsers: List<TopUsersByFormTypeResponse>) {
    var showAll by remember { mutableStateOf(false) }

    AwaqCard {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Header
            Text(
                text = "Cantidad de Formularios por Usuarios",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = AwaqGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // Contenido
            topUsers.forEach { tenantData ->
                // Header del tenant
                Text(
                    text = tenantData.tenant.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (tenantData.topUsers.isEmpty()) {
                    // No hay formularios
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        color = Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Sin formularios registrados",
                            fontSize = 14.sp,
                            color = Color(0xFF999999),
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    // Mostrar formularios
                    val displayUsers = if (showAll) tenantData.topUsers else tenantData.topUsers.take(3)

                    displayUsers.forEach { topUser ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
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
                                        text = topUser.formType.replaceFirstChar { it.uppercase() },
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = Color(0xFF333333)
                                    )
                                    Text(
                                        text = topUser.user.username,
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666)
                                    )
                                }
                                Surface(
                                    color = AwaqGreen,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${topUser.formCount}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Botón Show All si hay más de 3 formularios
                    if (tenantData.topUsers.size > 3 && !showAll) {
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = { showAll = true },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Show All (${tenantData.topUsers.size})",
                                color = AwaqGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Botón Show Less cuando está expandido
                    if (showAll && tenantData.topUsers.size > 3) {
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = { showAll = false },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Show Less",
                                color = AwaqGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
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