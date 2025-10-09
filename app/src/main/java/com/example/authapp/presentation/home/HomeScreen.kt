package com.example.authapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authapp.data.model.TodoDto
import com.example.authapp.ui.theme.AwaqGreen
import com.example.authapp.ui.theme.White
import com.example.authapp.ui.theme.Black

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToForm: () -> Unit,
    todoViewModel: TodoViewModel
) {
    val todoListState by todoViewModel.todoListState.collectAsState()

    LaunchedEffect(Unit) {
        todoViewModel.loadTodos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Encabezado ---
        Text(
            text = "Bienvenido a AWAQ 🌿",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AwaqGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gestiona tus tareas y recursos aquí",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- Botones principales ---
        Button(
            onClick = onNavigateToForm,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AwaqGreen,
                contentColor = White
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Ir al Formulario", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onLogout) {
            Text(
                "Cerrar Sesión",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TodoListItem(
    todo: TodoDto,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF8F8F8), shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = AwaqGreen,
                    uncheckedColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = todo.task,
                style = if (todo.isCompleted)
                    TextStyle(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                else TextStyle(color = Black, fontWeight = FontWeight.Medium)
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar tarea",
                tint = Color(0xFFD32F2F)
            )
        }
    }
}