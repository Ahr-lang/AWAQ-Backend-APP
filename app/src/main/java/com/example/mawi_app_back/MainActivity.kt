package com.example.mawi_app_back

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mawi_app_back.data.local.TokenManager
import com.example.mawi_app_back.data.AuthRepository
import com.example.mawi_app_back.data.remote.RetrofitClient
import com.example.mawi_app_back.navigation.AppNavigation
import com.example.mawi_app_back.presentation.AuthState
import com.example.mawi_app_back.presentation.AuthViewModel
import com.example.mawi_app_back.presentation.AuthViewModelFactory
import com.example.mawi_app_back.ui.theme.AuthAppTheme
import androidx.compose.runtime.collectAsState
import com.example.mawi_app_back.data.local.TenantManager

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(applicationContext)
        val apiService = RetrofitClient.create(tokenManager)
        val authRepository = AuthRepository(apiService, tokenManager)
        val viewModelFactory = AuthViewModelFactory(authRepository)

        enableEdgeToEdge()
        setContent {
            AuthAppTheme {
                val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
                val authState by authViewModel.authState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    when (authState) {
                        is AuthState.InitialLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }
                        }
                        else -> {
                            AppNavigation(
                                startDestination = if (authState is AuthState.Authenticated) "home" else "login",
                                authViewModel = authViewModel,
                                apiService = apiService
                            )
                        }
                    }
                }
            }
        }
    }
}
