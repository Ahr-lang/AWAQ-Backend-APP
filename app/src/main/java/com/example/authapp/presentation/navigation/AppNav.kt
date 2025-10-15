package com.example.authapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.presentation.auth.AuthViewModel
import com.example.authapp.presentation.auth.AuthViewModelFactory
import com.example.authapp.presentation.auth.LoginScreen
import com.example.authapp.presentation.auth.SignUpScreen

@Composable
fun AppNav(repository: AuthRepository) {
    val navController = rememberNavController()

    // Creamos los ViewModels solo una vez con factory
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository)
    )
    val statusViewModel: StatusViewModel = viewModel(
        factory = StatusViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
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
                authViewModel = authViewModel,
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
                onGoToStatus = { navController.navigate(Routes.STATUS) },
                onNavigateToAddUser = { tenant ->
                    navController.navigate("addUser/$tenant")
                }
            )
        }

        composable("addUser/{tenant}") { backStackEntry ->
            val tenant = backStackEntry.arguments?.getString("tenant") ?: ""
            AddUserScreen(
                tenant = tenant,
                statusViewModel = statusViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}