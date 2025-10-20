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
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    // Navega si ya está autenticado
    if (authState is AuthState.Authenticated) {
        LaunchedEffect(Unit) { onLoginSuccess() }
    }

    AwaqBackground {
        AwaqCard(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Branding
                AwaqHeader(
                    title = "Bienvenido a AWAQ",
                    subtitle = "Inicia sesión para continuar"
                )

                // Campo Email
                EmailTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                Spacer(Modifier.height(12.dp))

                // Campo Password
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
                val canSubmit = email.isNotBlank() && password.isNotBlank()

                AwaqPrimaryButton(
                    text = "Iniciar sesión",
                    onClick = { authViewModel.signIn(email.trim(), password) },
                    enabled = canSubmit,
                    isLoading = isLoading
                )

                // CTA Registro
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "¿No tienes cuenta? Regístrate",
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