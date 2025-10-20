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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.data.remote.models.UserDto

@Composable
fun UsersScreen(viewModel: UsersViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val tenants = viewModel.tenants
    var showDeleteDialog by remember { mutableStateOf(false) }
    var usernameToDelete by remember { mutableStateOf("") }

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
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(tenant)
                }
            }
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
                                        containerColor = MaterialTheme.colorScheme.errorContainer
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
    }
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
