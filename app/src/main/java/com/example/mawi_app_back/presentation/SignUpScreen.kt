package com.example.mawi_app_back.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.presentation.components.*
import com.example.mawi_app_back.ui.theme.AwaqGreen

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    // Navega al login cuando el registro es exitoso
    LaunchedEffect(authState) {
        if (authState is AuthState.RegisterSuccess) {
            onNavigateToLogin()
            authViewModel.resetState()
        }
    }

    AwaqBackground {
        AwaqCard(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo y títulos
                AwaqHeader(
                    title = "Crea tu cuenta AWAQ",
                    subtitle = "Únete para empezar a gestionar tus recursos"
                )

                // Username
                AwaqTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Nombre de usuario",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Email
                EmailTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Password
                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Error
                AnimatedVisibility(
                    visible = authState is AuthState.Error,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val message = (authState as? AuthState.Error)?.message ?: ""
                    ErrorMessage(
                        message = message,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Botón principal
                val isLoading = authState is AuthState.Loading
                val canSubmit = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()

                AwaqPrimaryButton(
                    text = "Registrarse",
                    onClick = { authViewModel.Register(username.trim(), email.trim(), password) },
                    enabled = canSubmit,
                    isLoading = isLoading
                )

                Spacer(Modifier.height(10.dp))

                // Ir al login
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        color = AwaqGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Overlay de carga
        LoadingOverlay(isVisible = authState is AuthState.Loading)
    }
}