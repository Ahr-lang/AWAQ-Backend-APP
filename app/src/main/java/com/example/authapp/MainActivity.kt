package com.example.authapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.global.util.TokenManager
import com.example.authapp.global.network.RetrofitClient
import com.example.authapp.presentation.auth.AuthState
import com.example.authapp.presentation.auth.AuthViewModel
import com.example.authapp.presentation.auth.AuthViewModelFactory
import com.example.authapp.presentation.mainViews.HomeScreen
import com.example.authapp.presentation.auth.LoginScreen
import com.example.authapp.ui.theme.AuthAppTheme
import androidx.compose.runtime.getValue
import com.example.authapp.domains.usecase.GetTodosUseCase
import com.example.authapp.domains.usecase.SubmitFormUseCase
import com.example.authapp.presentation.componentes.borrardespues.FormScreen
import com.example.authapp.presentation.componentes.borrardespues.FormViewModel
import com.example.authapp.presentation.componentes.borrardespues.FormViewModelFactory
import com.example.authapp.presentation.auth.SignUpScreen
import com.example.authapp.presentation.mainViews.borrardespues.TodoViewModel
import com.example.authapp.presentation.mainViews.borrardespues.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(applicationContext)
        val apiService = RetrofitClient.create(tokenManager)
        val authRepository = AuthRepository(apiService, tokenManager)
        val getTodosUseCase = GetTodosUseCase(authRepository)
        val todoViewModelFactory = TodoViewModelFactory(getTodosUseCase)

        // Crea el SubmitFormUseCase
        val submitFormUseCase = SubmitFormUseCase(authRepository) // <--- NUEVA LÍNEA

        val viewModelFactory = AuthViewModelFactory(authRepository)
        val formModelFactory  = FormViewModelFactory(submitFormUseCase)
        enableEdgeToEdge()
        setContent {
            AuthAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
                    val formViewModel: FormViewModel = viewModel(factory = formModelFactory)
                    val authState by authViewModel.authState.collectAsState()
                    val todoViewModel: TodoViewModel = viewModel(factory = todoViewModelFactory)

                    if (authState is AuthState.InitialLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppNavigation(
                            startDestination = if (authState is AuthState.Authenticated) "home" else "login",
                            authViewModel = authViewModel,
                            formViewModel = formViewModel,
                            todoViewModel = todoViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(startDestination: String, authViewModel: AuthViewModel, formViewModel: FormViewModel, todoViewModel: TodoViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            // Se maneja la navegación a "home" cuando el estado es autenticado
            if (authState is AuthState.Authenticated) {
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            LoginScreen( 
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        composable("form") {
            FormScreen(formViewModel = formViewModel,
            onSubmissionSuccess = { navController.popBackStack() },
            onNavigateBack = { navController.popBackStack() } )
        }

        composable("home") {
            if (authState !is AuthState.Authenticated) {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
            HomeScreen(onLogout = { authViewModel.signOut() }, onNavigateToForm = { navController.navigate("form")},todoViewModel = todoViewModel)
        }
    }
}