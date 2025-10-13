package com.example.authapp.presentation.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.authapp.presentation.componentes.UserCard
import com.example.authapp.ui.theme.AwaqGreen

@OptIn(ExperimentalMaterial3Api::class) //ESTO HAY Q CHECARLO ESTA DUDOSO
@Composable
fun VistaUsuarios() {
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
                item {
                    UserCard(
                        title = "Usuarios AGROMO",
                        users = listOf("Usuario 1", "Usuario 2", "Usuario 3"),
                        onAddClick = { /* TODO */ },
                        onDeleteClick = { /* TODO */ }
                    )
                }

                item {
                    UserCard(
                        title = "Usuarios BIOMO",
                        users = listOf("Usuario 1", "Usuario 2"),
                        onAddClick = { /* TODO */ },
                        onDeleteClick = { /* TODO */ }
                    )
                }

                item {
                    UserCard(
                        title = "Usuarios ROBORANGER",
                        users = listOf("Usuario 1"),
                        onAddClick = { /* TODO */ },
                        onDeleteClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}