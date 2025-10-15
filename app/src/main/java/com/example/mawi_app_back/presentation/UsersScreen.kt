package com.example.mawi_app_back.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.ui.theme.AwaqGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // dialogs visibility
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // tenant dropdown state
    var expanded by remember { mutableStateOf(false) }
    var selectedTenant by remember { mutableStateOf(viewModel.currentTenant) }

    LaunchedEffect(Unit) { viewModel.fetchUsers() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Usuarios por Tenant",
            style = MaterialTheme.typography.headlineSmall,
            color = AwaqGreen,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

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
                modifier = Modifier.menuAnchor()
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

        Spacer(Modifier.height(16.dp))

        // Actions row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { showAddDialog = true }) {
                Text("Agregar usuario")
            }
            OutlinedButton(onClick = { showDeleteDialog = true }) {
                Text("Eliminar usuario")
            }
        }

        Spacer(Modifier.height(16.dp))

        // List section that fills remaining space
        when (val s = uiState) {
            is UsersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AwaqGreen)
                }
            }

            is UsersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        s.message,
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            is UsersUiState.Success -> {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(s.users, key = { index, user -> "${user.username}-$index" }) { _, user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
                        ) {
                            Text(
                                text = user.username,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            UsersUiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay usuarios disponibles.")
                }
            }
        }
    }

    // --- Add user dialog ---
    if (showAddDialog) {
        AddUserDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { username, email, password ->
                viewModel.addUser(username, email, password)
                showAddDialog = false
            }
        )
    }

    // --- Delete user dialog ---
    if (showDeleteDialog) {
        DeleteUserDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = { username ->
                viewModel.deleteUser(username)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
private fun AddUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (username: String, email: String, password: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(username.trim(), email.trim(), password) }) {
                Text("Crear")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DeleteUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (username: String) -> Unit
) {
    var username by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    singleLine = true
                )
                Text(
                    "Se eliminará el usuario en el tenant seleccionado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
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
        }
    )
}
