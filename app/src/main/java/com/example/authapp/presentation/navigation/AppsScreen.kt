package com.example.authapp.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier

@Composable
fun AppsScreen(
    onGoToStatus: () -> Unit,
    onGoToUsers: () -> Unit,
    statusViewModel: StatusViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onGoToStatus) {
            Text("Ir a STATUS")
        }
        Button(onClick = onGoToUsers) {
            Text("Ir a USERS")
        }
    }
}