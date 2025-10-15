package com.example.authapp.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UsersScreen(
    statusViewModel: StatusViewModel,
    onGoToApps: () -> Unit,
    onGoToStatus: () -> Unit,
    onNavigateToAddUser: (String) -> Unit
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
            item {
                UserCard(
                    title = "Agromo",
                    users = agromoUsers.map { it.username },
                    onAddClick = onNavigateToAddUser,
                    onDeleteClick = { username ->
                        val user = agromoUsers.find { it.username == username }
                        if (user != null) statusViewModel.deleteUser("agromo", user.email)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                UserCard(
                    title = "Biomo",
                    users = biomoUsers.map { it.username },
                    onAddClick = onNavigateToAddUser,
                    onDeleteClick = { username ->
                        val user = biomoUsers.find { it.username == username }
                        if (user != null) statusViewModel.deleteUser("biomo", user.email)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                UserCard(
                    title = "Robo",
                    users = roboUsers.map { it.username },
                    onAddClick = onNavigateToAddUser,
                    onDeleteClick = { username ->
                        val user = roboUsers.find { it.username == username }
                        if (user != null) statusViewModel.deleteUser("robo", user.email)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onGoToApps) { Text("Ir a APPS") }
            Button(onClick = onGoToStatus) { Text("Ir a STATUS") }
        }
    }
}