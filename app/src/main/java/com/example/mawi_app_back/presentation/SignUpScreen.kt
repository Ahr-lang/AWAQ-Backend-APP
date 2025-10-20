package com.example.mawi_app_back.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.R
import com.example.mawi_app_back.ui.theme.AwaqGreen
import com.example.mawi_app_back.ui.theme.White

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()

    // Colores derivados (coherentes con Login)
    val Ink = Color(0xFF111111)
    val SubtleText = Color(0xFF50565A)
    val FieldBorder = Color(0xFFCBD5D1)
    val GreenSoft = AwaqGreen.copy(alpha = 0.08f)

    // Navega al login cuando el registro es exitoso
    LaunchedEffect(authState) {
        if (authState is AuthState.RegisterSuccess) {
            onNavigateToLogin()
            authViewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(GreenSoft, Color.White)))
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .shadow(10.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.awaq),
                    contentDescription = "Logo Awaq",
                    modifier = Modifier
                        .height(96.dp)
                        .padding(bottom = 8.dp)
                )

                // Título y subtítulo
                Text(
                    text = "Crea tu cuenta AWAQ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink
                )
                Text(
                    text = "Únete para empezar a gestionar tus recursos",
                    color = SubtleText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwaqGreen,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = AwaqGreen,
                        cursorColor = AwaqGreen,
                        focusedTextColor = Ink,
                        unfocusedTextColor = Ink
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwaqGreen,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = AwaqGreen,
                        cursorColor = AwaqGreen,
                        focusedTextColor = Ink,
                        unfocusedTextColor = Ink
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Password (toggle sin íconos para evitar dependencias)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                if (passwordVisible) "Ocultar" else "Mostrar",
                                color = AwaqGreen,
                                fontSize = 12.sp
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwaqGreen,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = AwaqGreen,
                        cursorColor = AwaqGreen,
                        focusedTextColor = Ink,
                        unfocusedTextColor = Ink
                    )
                )

                // Error lindo y legible
                AnimatedVisibility(
                    visible = authState is AuthState.Error,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val message = (authState as? AuthState.Error)?.message ?: ""
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Botón principal
                val isLoading = authState is AuthState.Loading
                val canSubmit = username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && !isLoading

                Button(
                    onClick = { authViewModel.Register(username.trim(), email.trim(), password) },
                    enabled = canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AwaqGreen,
                        contentColor = White,
                        disabledContainerColor = AwaqGreen.copy(alpha = 0.35f),
                        disabledContentColor = White.copy(alpha = 0.9f)
                    )
                ) {
                    Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

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
        AnimatedVisibility(
            visible = authState is AuthState.Loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AwaqGreen)
            }
        }
    }
}