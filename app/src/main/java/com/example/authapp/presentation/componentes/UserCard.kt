package com.example.authapp.presentation.componentes

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val AwaqGreen = Color(0xFF2B5629)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

@Composable
fun UserCard(
    title: String,
    users: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = AwaqGreen)

        Spacer(modifier = Modifier.height(8.dp))

        users.forEach { user ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = user)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onDeleteClick(user) },
                        colors = ButtonDefaults.buttonColors(containerColor = AwaqGreen)
                    ) {
                        Text(text = "Eliminar", color = White)
                    }

                    Button(
                        onClick = { onAddClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = AwaqGreen)
                    ) {
                        Text(text = "Agregar", color = White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}