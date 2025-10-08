package com.example.authapp.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authapp.R
import com.example.authapp.presentation.auth.AuthState
import com.example.authapp.presentation.auth.AuthViewModel
import com.example.authapp.ui.theme.AwaqGreen
import com.example.authapp.ui.theme.Black
import com.example.authapp.ui.theme.White

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Logo Awaq ---
        Image(
            painter = painterResource(id = R.drawable.awaq),
            contentDescription = "Logo Awaq",
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 24.dp)
        )

        // --- Título ---
        Text(
            text = "Crea tu cuenta AWAQ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AwaqGreen
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Email ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AwaqGreen,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = AwaqGreen,
                cursorColor = AwaqGreen
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Contraseña ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val imageVector = if (passwordVisible) Icons.Filled.Close else Icons.Filled.FavoriteBorder
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = imageVector, contentDescription = description, tint = AwaqGreen)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AwaqGreen,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = AwaqGreen,
                cursorColor = AwaqGreen
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botón principal ---
        if (authState is AuthState.Loading) {
            CircularProgressIndicator(color = AwaqGreen)
        } else {
            Button(
                onClick = { authViewModel.register(email, email, password, "back") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AwaqGreen,
                    contentColor = White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Botón para ir al login ---
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = AwaqGreen,
                fontWeight = FontWeight.SemiBold
            )
        }

        // --- Efecto para regresar al login tras registro exitoso ---
        LaunchedEffect(authState) {
            if (authState is AuthState.SignUpSuccess) {
                onNavigateToLogin()
                authViewModel.resetState()
            }
        }
    }
}