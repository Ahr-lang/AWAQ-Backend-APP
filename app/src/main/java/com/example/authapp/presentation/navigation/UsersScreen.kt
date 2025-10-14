package com.example.authapp.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier

@Composable
fun UsersScreen(
    onGoToApps: () -> Unit,
    onGoToStatus: () -> Unit,
    statusViewModel: StatusViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onGoToApps) {
            Text("Ir a APPS")
        }
        Button(onClick = onGoToStatus) {
            Text("Ir a STATUS")
        }
    }
}
