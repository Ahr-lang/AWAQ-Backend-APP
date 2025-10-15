package com.example.authapp.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusScreen(
    statusViewModel: StatusViewModel,
    onGoToApps: () -> Unit,
    onGoToUsers: () -> Unit
) {
    val agromoUsers by statusViewModel.agromoUsers.collectAsState()
    val biomoUsers by statusViewModel.biomoUsers.collectAsState()
    val roboUsers by statusViewModel.roboUsers.collectAsState()

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                UserCard(
                    title = "Usuarios Agromo",
                    users = agromoUsers.map { it.username },
                    onAddClick = { /* pantalla agregar */ },
                    onDeleteClick = { username ->
                        statusViewModel.deleteUser("agromo", username)
                    }
                )
            }

            item {
                UserCard(
                    title = "Usuarios Biomo",
                    users = biomoUsers.map { it.username },
                    onAddClick = { /* abrir diálogo agregar */ },
                    onDeleteClick = { username ->
                        statusViewModel.deleteUser("biomo", username)
                    }
                )
            }

            item {
                UserCard(
                    title = "Usuarios Robo",
                    users = roboUsers.map { it.username },
                    onAddClick = { /* abrir diálogo agregar */ },
                    onDeleteClick = { username ->
                        statusViewModel.deleteUser("robo", username)
                    }
                )
            }
        }
    }
}
