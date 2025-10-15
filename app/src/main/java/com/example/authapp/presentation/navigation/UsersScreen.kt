package com.example.authapp.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UsersScreen(
    statusViewModel: StatusViewModel,
    onGoToApps: () -> Unit,
    onGoToStatus: () -> Unit
) {
    val agromoUsers by statusViewModel.agromoUsers.collectAsState()
    val biomoUsers by statusViewModel.biomoUsers.collectAsState()
    val roboUsers by statusViewModel.roboUsers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            // 🔹 Sección Agromo
            item {
                Text("Agromo", style = MaterialTheme.typography.titleMedium)
                Button(onClick = {
                    // Aquí puedes abrir un diálogo o pantalla para pedir username/email/password
                    statusViewModel.addUser("agromo", "nuevoUser", "nuevo@email.com", "123456")
                }) {
                    Text("Agregar usuario")
                }
            }
            items(agromoUsers) { user ->
                UserCard(user, onDelete = { statusViewModel.deleteUser("agromo", user.email) })
            }

            // 🔹 Sección Biomo
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Biomo", style = MaterialTheme.typography.titleMedium)
                Button(onClick = {
                    statusViewModel.addUser("biomo", "nuevoUser", "nuevo@email.com", "123456")
                }) {
                    Text("Agregar usuario")
                }
            }
            items(biomoUsers) { user ->
                UserCard(user, onDelete = { statusViewModel.deleteUser("biomo", user.email) })
            }

            // 🔹 Sección Robo
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Robo", style = MaterialTheme.typography.titleMedium)
                Button(onClick = {
                    statusViewModel.addUser("robo", "nuevoUser", "nuevo@email.com", "123456")
                }) {
                    Text("Agregar usuario")
                }
            }
            items(roboUsers) { user ->
                UserCard(user, onDelete = { statusViewModel.deleteUser("robo", user.email) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botones de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onGoToApps) { Text("Ir a APPS") }
            Button(onClick = onGoToStatus) { Text("Ir a STATUS") }
        }
    }
}

@Composable
fun UserCard(user: User, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(user.username)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar usuario")
            }
        }
    }
}