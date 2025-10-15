package com.example.authapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val AwaqGreen = Color(0xFF2B5629)
val White = Color(0xFFFFFFFF)

@Composable
fun UserCard(
    title: String, // tenant: "Agromo", "Biomo", "Robo"
    users: List<String>,
    onAddClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = AwaqGreen)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onAddClick(title) },
            colors = ButtonDefaults.buttonColors(containerColor = AwaqGreen),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar usuario", color = White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (users.isEmpty()) {
            Text("No hay usuarios aún", color = AwaqGreen)
        } else {
            users.forEach { username ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(username, color = AwaqGreen)

                    Button(
                        onClick = { onDeleteClick(username) },
                        colors = ButtonDefaults.buttonColors(containerColor = AwaqGreen)
                    ) {
                        Text("Eliminar", color = White)
                    }
                }
            }
        }
    }
}