package com.example.authapp.presentation.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authapp.presentation.navigation.UserCard
import com.example.authapp.ui.theme.AwaqGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaUsuarios(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado de Usuarios", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AwaqGreen
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🔹 Usuarios AGROMO
                item {
                    UserCard(
                        title = "Agromo",
                        users = listOf("Usuario 1", "Usuario 2", "Usuario 3"),
                        onAddClick = { tenant ->
                            navController.navigate("addUser/agromo")
                        },
                        onDeleteClick = { username ->
                            // TODO: implementar eliminar usuario
                        }
                    )
                }

                // 🔹 Usuarios BIOMO
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    UserCard(
                        title = "Biomo",
                        users = listOf("Usuario 1", "Usuario 2"),
                        onAddClick = { tenant ->
                            navController.navigate("addUser/biomo")
                        },
                        onDeleteClick = { username ->
                            // TODO: implementar eliminar usuario
                        }
                    )
                }

                // 🔹 Usuarios ROBORANGER
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    UserCard(
                        title = "RoboRanger",
                        users = listOf("Usuario 1"),
                        onAddClick = { tenant ->
                            navController.navigate("addUser/robo")
                        },
                        onDeleteClick = { username ->
                            // TODO: implementar eliminar usuario
                        }
                    )
                }
            }
        }
    }
}