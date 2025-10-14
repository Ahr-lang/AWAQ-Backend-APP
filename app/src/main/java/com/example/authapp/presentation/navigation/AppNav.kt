package com.example.authapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.presentation.auth.LoginScreen
import com.example.authapp.presentation.auth.SignUpScreen

@Composable
fun AppNav(repository: AuthRepository) {
    val navController = rememberNavController()
    val statusViewModel: StatusViewModel = viewModel(
        factory = StatusViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "login" // pantalla inicial
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = viewModel(),
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = {
                    navController.navigate(Routes.STATUS) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                authViewModel = viewModel(),
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        composable(Routes.STATUS) {
            StatusScreen(
                statusViewModel = statusViewModel,
                onGoToApps = { navController.navigate(Routes.APPS) },
                onGoToUsers = { navController.navigate(Routes.USERS) }
            )
        }

        composable(Routes.APPS) {
            AppsScreen(
                statusViewModel = statusViewModel,
                onGoToStatus = { navController.navigate(Routes.STATUS) },
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