package com.example.mawi_app_back.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.R
import com.example.mawi_app_back.ui.theme.AwaqGreen
import com.example.mawi_app_back.ui.theme.White

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    // Navega si ya está autenticado
    if (authState is AuthState.Authenticated) {
        LaunchedEffect(Unit) { onLoginSuccess() }
    }

    // Paleta derivada suave (sin tocar tu theme global)
    val Ink = Color(0xFF111111)
    val SubtleText = Color(0xFF50565A)
    val FieldBorder = Color(0xFFCBD5D1) // gris tibio para bordes sin focus
    val GreenSoft = AwaqGreen.copy(alpha = 0.08f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(GreenSoft, Color.White)
                )
            )
            .padding(20.dp)
    ) {
        // Contenido centrado en una Card estilizada
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Branding
                Image(
                    painter = painterResource(id = R.drawable.awaq),
                    contentDescription = "Logo Awaq",
                    modifier = Modifier
                        .height(96.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Bienvenido a AWAQ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Ink,
                    letterSpacing = 0.2.sp
                )
                Text(
                    text = "Inicia sesión para continuar",
                    color = SubtleText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "email",
                            tint = AwaqGreen
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwaqGreen,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = AwaqGreen,
                        cursorColor = AwaqGreen,
                        focusedLeadingIconColor = AwaqGreen,
                        unfocusedLeadingIconColor = AwaqGreen.copy(alpha = 0.9f),
                        focusedTextColor = Ink,
                        unfocusedTextColor = Ink
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Campo Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "contraseña",
                            tint = AwaqGreen
                        )
                    },
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
                        val desc =
                            if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = desc, tint = SubtleText)
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AwaqGreen,
                        unfocusedBorderColor = FieldBorder,
                        focusedLabelColor = AwaqGreen,
                        cursorColor = AwaqGreen,
                        focusedLeadingIconColor = AwaqGreen,
                        unfocusedLeadingIconColor = AwaqGreen.copy(alpha = 0.9f),
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
                val canSubmit = email.isNotBlank() && password.isNotBlank() && !isLoading

                Button(
                    onClick = { authViewModel.signIn(email.trim(), password) },
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
                    Text(
                        "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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

        // Overlay de carga (cuando Loading)
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