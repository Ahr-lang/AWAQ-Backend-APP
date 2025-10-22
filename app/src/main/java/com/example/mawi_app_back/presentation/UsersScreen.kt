package com.example.mawi_app_back.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.data.remote.models.UserDto
import com.example.mawi_app_back.ui.theme.AwaqGreen

import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun UsersScreen(viewModel: UsersViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val tenants = viewModel.tenants
    var showDeleteDialog by remember { mutableStateOf(false) }
    var usernameToDelete by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    // Outer column no longer scrolls
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Administrar Usuarios",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Tenant selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tenants.forEach { tenant ->
                Button(
                    onClick = { viewModel.setTenant(tenant) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tenant == viewModel.currentTenant)
                            AwaqGreen
                        else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(tenant)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // boton para agregar usuario
        Button(
            onClick = { showAddDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = AwaqGreen,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Agregar Usuario")
        }


        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UsersUiState.Loading -> {
                CircularProgressIndicator()
            }

            is UsersUiState.Error -> {
                val msg = (uiState as UsersUiState.Error).message
                Text("Error: $msg", color = MaterialTheme.colorScheme.error)
            }

            is UsersUiState.Success -> {
                val successState = uiState as UsersUiState.Success
                val users = successState.users

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(users) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(user.username, fontWeight = FontWeight.Bold)
                                    Text(user.user_email ?: "")
                                }
                                Button(
                                    onClick = {
                                        usernameToDelete = user.username
                                        showDeleteDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red,        // color de fondo
                                        contentColor = Color.White         // color del texto
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }

                if (showDeleteDialog) {
                    DeleteUserDialog(
                        initialUsername = usernameToDelete,
                        onDismiss = { showDeleteDialog = false },
                        onConfirm = { username ->
                            val user = users.find { it.username == username }
                            user?.let {
                                viewModel.deleteUser(it.id ?: return@let, it.username)
                                showDeleteDialog = false
                            }
                        }
                    )
                }
            }

            UsersUiState.Idle -> {
                Text("Selecciona un tenant para ver usuarios.")
            }
        }

        // Para agregar usuario
        if (showAddDialog) {
            AddUserDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { username, email, password ->
                    viewModel.addUser(username, email, password)
                    showAddDialog = false
                }
            )
        }
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
fun DeleteUserDialog(
    initialUsername: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var username by remember { mutableStateOf(initialUsername) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Eliminar usuario") },
        text = {
            Column {
                Text("¿Estás seguro de eliminar el usuario \"$initialUsername\"?")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(username)
                }
            ) {
                Text("Eliminar", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}