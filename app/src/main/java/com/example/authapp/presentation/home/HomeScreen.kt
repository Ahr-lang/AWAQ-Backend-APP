package com.example.authapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.example.authapp.data.model.TodoDto

@Composable
fun HomeScreen(
    onLogout: () -> Unit ,
    onNavigateToForm: () -> Unit,
    onNavigateToCamera: () -> Unit,
    todoViewModel: TodoViewModel
) {
    val todoListState by todoViewModel.todoListState.collectAsState()
    LaunchedEffect(key1 = Unit) { // O key1 = authState si depende de eso
        todoViewModel.loadTodos()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡Bienvenido a la pantalla de inicio!")
        Button(onClick = onNavigateToForm) { // Botón para ir al formulario
            Text("Ir al formulario")
        }
        Button(
            onClick = onNavigateToCamera, // <--- LLAMA AL NUEVO CALLBACK
            modifier = Modifier.padding(top = 8.dp) // Espaciado
        ) {
            Text("Tomar Foto (Cámara)")
        }
        Button(
            onClick = onLogout,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Cerrar Sesión")
        }
        // Mostrar lista de tareas, cargador o error
        when (val state = todoListState) {
            is TodoListState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is TodoListState.Success -> {
                if (state.todos.isEmpty()) {
                    Text("No tienes tareas pendientes. ¡Añade una!")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.todos, key = { it.id }) { todo ->
                            TodoListItem(
                                todo = todo,
                                onToggleComplete = {  },
                                onDelete = {  }
                            )

                        }
                    }
                }
            }
            is TodoListState.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                Button(onClick = { todoViewModel.loadTodos() }) {
                    Text("Reintentar")
                }
            }
            is TodoListState.Idle -> {
                // Puede ser el estado inicial antes de que se active el LaunchedEffect
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Text("Cargando tareas...")
                }
            }
        }
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggleComplete() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = todo.task,
            modifier = Modifier.weight(1f),
            style = if (todo.isCompleted) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle.Default
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar tarea", tint = MaterialTheme.colorScheme.error)
        }
    }
}