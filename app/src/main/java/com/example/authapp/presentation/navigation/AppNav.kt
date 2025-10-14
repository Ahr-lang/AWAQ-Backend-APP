package com.example.authapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val statusViewModel: StatusViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.STATUS //
    ) {
        composable(Routes.APPS) {
            AppsScreen(
                statusViewModel = statusViewModel,
                onGoToStatus = { navController.navigate(Routes.STATUS) },
                onGoToUsers = { navController.navigate(Routes.USERS) }
            )
        }

        composable(Routes.STATUS) {
            StatusScreen(
                statusViewModel = statusViewModel,
                onGoToApps = { navController.navigate(Routes.APPS) },
                onGoToUsers = { navController.navigate(Routes.USERS) }
            )
        }

        composable(Routes.USERS) {
            UsersScreen(
                statusViewModel = statusViewModel,
                onGoToApps = { navController.navigate(Routes.APPS) },
                onGoToStatus = { navController.navigate(Routes.STATUS) }
            )
        }
    }
}
