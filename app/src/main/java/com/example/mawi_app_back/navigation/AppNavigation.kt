package com.example.mawi_app_back.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mawi_app_back.data.UsersRepository
import com.example.mawi_app_back.data.HomeRepository
import com.example.mawi_app_back.data.local.TenantManager
import com.example.mawi_app_back.data.remote.AuthApiService
import com.example.mawi_app_back.domain.usecase.AddUserUseCase
import com.example.mawi_app_back.domain.usecase.DeleteUserUseCase
import com.example.mawi_app_back.domain.usecase.GetUsersByTenantUseCase
import com.example.mawi_app_back.domain.usecase.GetHomeDataUseCase
import com.example.mawi_app_back.presentation.AuthState
import com.example.mawi_app_back.presentation.AuthViewModel
import com.example.mawi_app_back.presentation.HomeScreen
import com.example.mawi_app_back.presentation.LoginScreen
import com.example.mawi_app_back.presentation.SignUpScreen
import com.example.mawi_app_back.presentation.UsersScreen
import com.example.mawi_app_back.presentation.UsersViewModel
import com.example.mawi_app_back.presentation.UsersViewModelFactory
import com.example.mawi_app_back.ui.BottomBar
import com.example.mawi_app_back.data.StatusRepository
import com.example.mawi_app_back.domain.usecase.GetStatusDashboardUseCase
import com.example.mawi_app_back.presentation.StatusViewModel
import com.example.mawi_app_back.presentation.StatusViewModelFactory
import com.example.mawi_app_back.presentation.StatusScreen
import com.example.mawi_app_back.presentation.HomeViewModelFactory
import com.example.mawi_app_back.presentation.HomeViewModel

@Composable
fun AppNavigation(
    startDestination: String,
    authViewModel: AuthViewModel,
    apiService: AuthApiService
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    // Tabs where bottom bar is visible
    val routesWithBottomBar = setOf(
        BottomNavItem.Home.route,
        BottomNavItem.Tasks.route,
        BottomNavItem.Users.route
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in routesWithBottomBar

    val usersVmFactory = remember {
        val repo = UsersRepository(apiService)
        val getUC = GetUsersByTenantUseCase(repo)
        val addUC = AddUserUseCase(repo)
        val delUC = DeleteUserUseCase(repo)
        UsersViewModelFactory(getUC, addUC, delUC)
    }

    val statusVmFactory = remember {
        val statusRepo = StatusRepository(apiService)
        val getStatusUC = GetStatusDashboardUseCase(statusRepo)
        StatusViewModelFactory(getStatusUC)
    }

    val homeVmFactory = remember {
        val homeRepo = HomeRepository(apiService)
        val getHomeDataUC = GetHomeDataUseCase(homeRepo)
        HomeViewModelFactory(getHomeDataUC)
    }

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // -------------------- AUTH GRAPH --------------------
            composable("login") {
                if (authState is AuthState.Authenticated) {
                    LaunchedEffect(Unit) {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("register") {
                if (authState is AuthState.Authenticated) {
                    LaunchedEffect(Unit) {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo("register") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
                SignUpScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }

            // -------------------- MAIN TABS --------------------
            composable(BottomNavItem.Home.route) {
                if (authState !is AuthState.Authenticated) {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo(BottomNavItem.Home.route) { inclusive = true }
                        }
                    }
                } else {
                    val vm: HomeViewModel = viewModel(factory = homeVmFactory)
                    HomeScreen(
                        viewModel = vm,
                        onLogout = {
                            authViewModel.signOut()
                            // Navigate immediately to login and clear back stack
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            composable(BottomNavItem.Tasks.route) {
                if (authState !is AuthState.Authenticated) {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                } else {
                    val vm: StatusViewModel = viewModel(factory = statusVmFactory)
                    StatusScreen(viewModel = vm)
                }
            }

            composable(BottomNavItem.Users.route) {
                if (authState !is AuthState.Authenticated) {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                } else {
                    val vm: UsersViewModel = viewModel(factory = usersVmFactory)
                    UsersScreen(viewModel = vm)
                }
            }
        }
    }
}

// Simple placeholder for the Tasks tab to keep this file self-contained.
// Replace with your actual Tasks composable.
@Composable
private fun TasksPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
        // Or a simple label:
        // Text("Tasks", style = MaterialTheme.typography.headlineSmall)
    }
}
