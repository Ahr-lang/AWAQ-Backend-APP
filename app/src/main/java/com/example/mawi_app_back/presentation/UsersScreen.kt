package com.example.mawi_app_back.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.ui.theme.AwaqGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // UI state
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var usernameToDelete by remember { mutableStateOf("") }

    // Tenant selector
    var expanded by remember { mutableStateOf(false) }
    var selectedTenant by remember { mutableStateOf(viewModel.currentTenant) }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Colores coherentes con Login/SignUp
    val Ink = Color(0xFF111111)
    val SubtleText = Color(0xFF50565A)
    val FieldBorder = Color(0xFFCBD5D1)
    val GreenSoft = AwaqGreen.copy(alpha = 0.08f)
    val DividerSoft = Color(0xFFE6E8EA)

    // Cargar usuarios al entrar y al cambiar tenant
    LaunchedEffect(Unit) { viewModel.fetchUsers() }

    // Mostrar snackbar cuando llegue un mensaje de éxito
    LaunchedEffect(uiState) {
        val msg = (uiState as? UsersUiState.Success)?.message
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(GreenSoft, Color.White)))
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Usuarios por Tenant",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Administra cuentas en el tenant seleccionado",
                    color = SubtleText,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(16.dp))

                // Card: Tenant selector + acciones
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Text(
                            text = "Configuración",
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )

                        Spacer(Modifier.height(12.dp))

                        // Tenant selector
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedTenant.uppercase(),
                                onValueChange = {},
                                label = { Text("Tenant") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AwaqGreen,
                                    unfocusedBorderColor = FieldBorder,
                                    focusedLabelColor = AwaqGreen,
                                    cursorColor = AwaqGreen
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                viewModel.tenants.forEach { t ->
                                    DropdownMenuItem(
                                        text = { Text(t.uppercase()) },
                                        onClick = {
                                            selectedTenant = t
                                            expanded = false
                                            viewModel.setTenant(t)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))
                        Divider(color = DividerSoft)
                        Spacer(Modifier.height(14.dp))

                        // Acciones
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showAddDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AwaqGreen,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Agregar usuario", fontWeight = FontWeight.Medium)
                            }
                            OutlinedButton(
                                onClick = {
                                    usernameToDelete = ""
                                    showDeleteDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AwaqGreen),
                                border = ButtonDefaults.outlinedButtonBorder
                            ) {
                                Text("Eliminar usuario", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Card: Lista de usuarios
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Usuarios (${(uiState as? UsersUiState.Success)?.users?.size ?: 0})",
                            fontWeight = FontWeight.SemiBold,
                            color = Ink
                        )
                        Spacer(Modifier.height(8.dp))

                        when (val s = uiState) {
                            is UsersUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = AwaqGreen)
                                }
                            }

                            is UsersUiState.Error -> {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    tonalElevation = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = s.message,
                                        modifier = Modifier.padding(12.dp),
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            is UsersUiState.Success -> {
                                if (s.users.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No hay usuarios disponibles.", color = SubtleText)
                                    }
                                } else {
                                    val listState = rememberLazyListState()
                                    LazyColumn(
                                        state = listState,
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(vertical = 4.dp)
                                    ) {
                                        itemsIndexed(
                                            s.users,
                                            key = { index, user -> "${user.username}-$index" }
                                        ) { _, user ->
                                            Surface(
                                                shape = RoundedCornerShape(16.dp),
                                                color = Color(0xFFF7FAF8),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text(
                                                            text = user.username,
                                                            fontWeight = FontWeight.Medium,
                                                            color = Ink
                                                        )
                                                        if (!user.user_email.isNullOrBlank()) {
                                                            Spacer(Modifier.height(2.dp))
                                                            Text(
                                                                text = user.user_email,
                                                                color = SubtleText,
                                                                fontSize = 13.sp
                                                            )
                                                        }
                                                    }
                                                    // Botón eliminar por fila (sin íconos → total compatibilidad)
                                                    TextButton(
                                                        onClick = {
                                                            usernameToDelete = user.username
                                                            showDeleteDialog = true
                                                        }
                                                    ) {
                                                        Text(
                                                            "Eliminar",
                                                            color = MaterialTheme.colorScheme.error,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            UsersUiState.Idle -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No hay usuarios disponibles.", color = SubtleText)
                                }
                            }
                        }
                    }
                }
            }

            // Overlay de carga global (bloquea toques)
            AnimatedVisibility(
                visible = uiState is UsersUiState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AwaqGreen)
                }
            }
        }
    }

    // Diálogo: Agregar usuario
    if (showAddDialog) {
        AddUserDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { username, email, password ->
                viewModel.addUser(username, email, password)
                showAddDialog = false
            },
            awaqGreen = AwaqGreen,
            fieldBorder = FieldBorder
        )
    }

    // Diálogo: Eliminar usuario (prellenado si viene de fila)
    if (showDeleteDialog) {
        DeleteUserDialog(
            initialUsername = usernameToDelete,
            onDismiss = { showDeleteDialog = false },
            onConfirm = { username ->
                viewModel.deleteUser(username)
                showDeleteDialog = false
            },
            awaqGreen = AwaqGreen,
            fieldBorder = FieldBorder
        )
    }
}

/* ===========================
   Diálogos con estética AWAQ
   =========================== */

@Composable
private fun AddUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (username: String, email: String, password: String) -> Unit,
    awaqGreen: Color,
    fieldBorder: Color
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(username.trim(), email.trim(), password) }) {
                Text("Crear")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Agregar usuario", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = awaqGreen,
                        unfocusedBorderColor = fieldBorder,
                        focusedLabelColor = awaqGreen,
                        cursorColor = awaqGreen
                    )
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = awaqGreen,
                        unfocusedBorderColor = fieldBorder,
                        focusedLabelColor = awaqGreen,
                        cursorColor = awaqGreen
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = awaqGreen,
                        unfocusedBorderColor = fieldBorder,
                        focusedLabelColor = awaqGreen,
                        cursorColor = awaqGreen
                    )
                )
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun DeleteUserDialog(
    initialUsername: String,
    onDismiss: () -> Unit,
    onConfirm: (username: String) -> Unit,
    awaqGreen: Color,
    fieldBorder: Color
) {
    var username by remember { mutableStateOf(initialUsername) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(username.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Eliminar usuario", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = awaqGreen,
                        unfocusedBorderColor = fieldBorder,
                        focusedLabelColor = awaqGreen,
                        cursorColor = awaqGreen
                    )
                )
                Text(
                    "Se eliminará el usuario en el tenant seleccionado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}