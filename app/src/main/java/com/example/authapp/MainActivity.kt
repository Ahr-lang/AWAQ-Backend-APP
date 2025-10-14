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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authapp.data.repository.AuthRepository
import com.example.authapp.global.util.TokenManager
import com.example.authapp.global.network.RetrofitClient
import com.example.authapp.presentation.auth.AuthState
import com.example.authapp.presentation.auth.AuthViewModel
import com.example.authapp.presentation.auth.AuthViewModelFactory
import com.example.authapp.presentation.auth.LoginScreen
import com.example.authapp.presentation.auth.SignUpScreen
import com.example.authapp.presentation.navigation.AppNav
import com.example.authapp.ui.theme.AuthAppTheme

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
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
                    val authState by authViewModel.authState.collectAsState()

                    if (authState is AuthState.InitialLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppNav() // <-- tu nueva navegación como root
                    }
                }
            }
        }
    }
}