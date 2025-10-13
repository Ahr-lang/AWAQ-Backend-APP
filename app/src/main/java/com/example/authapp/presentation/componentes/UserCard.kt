package com.example.authapp.presentation.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta AWAQ
val AwaqGreen = Color(0xFF2B5629)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

@Composable
fun UserCard(
    title: String,
    users: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent)
        ) {
            // Título
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AwaqGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de usuarios
            LazyColumn {
                items(users.size) { index ->
                    Text(
                        text = "• ${users[index]}",
                        fontSize = 16.sp,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = White)
                }

                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AwaqGreen)
                ) {
                    Text("Agregar", color = White)
                }
            }
        }
    }
}