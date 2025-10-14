package com.example.authapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp.presentation.navigation.UsersScreen
import com.example.authapp.presentation.navigation.AppsScreen
import com.example.authapp.presentation.navigation.StatusScreen
import com.example.authapp.presentation.navigation.StatusViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val statusViewModel: StatusViewModel = viewModel() // 🔥 CREA EL VIEWMODEL AQUÍ

    NavHost(
        navController = navController,
        startDestination = Routes.STATUS // Puedes cambiarlo a APPS si quieres
    ) {
        composable(Routes.APPS) {
            AppsScreen(
                onGoToStatus = { navController.navigate(Routes.STATUS) },
                onGoToUsers = { navController.navigate(Routes.USERS) }
            )
        }

        composable(Routes.STATUS) {
            // 🔥 AQUÍ LE PASAS EL VIEWMODEL COMO PARÁMETRO
            StatusScreen(
                statusViewModel = statusViewModel,
                onGoToApps = { navController.navigate(Routes.APPS) },
                onGoToUsers = { navController.navigate(Routes.USERS) }
            )
        }

        composable(Routes.USERS) {
            UsersScreen(
                onGoToApps = { navController.navigate(Routes.APPS) },
                onGoToStatus = { navController.navigate(Routes.STATUS) }
            )
        }
    }
}
