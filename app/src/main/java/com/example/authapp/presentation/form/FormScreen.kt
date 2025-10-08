package com.example.authapp.presentation.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authapp.R
import com.example.authapp.presentation.form.FormViewModel
import com.example.authapp.ui.theme.AwaqGreen
import com.example.authapp.ui.theme.Black
import com.example.authapp.ui.theme.White
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun FormScreen(
    formViewModel: FormViewModel,
    onSubmissionSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var field1 by remember { mutableStateOf("") }

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
            text = "Formulario AWAQ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AwaqGreen
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Campo de texto ---
        OutlinedTextField(
            value = field1,
            onValueChange = { field1 = it },
            label = { Text("Campo 1") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AwaqGreen,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = AwaqGreen,
                cursorColor = AwaqGreen
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botón Enviar ---
        Button(
            onClick = {
                formViewModel.submit(field1 = field1)
                onSubmissionSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AwaqGreen,
                contentColor = White
            ),
            shape = androidx.compose.material3.MaterialTheme.shapes.medium
        ) {
            Text("Enviar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Botón Regresar ---
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Black
            ),
            shape = androidx.compose.material3.MaterialTheme.shapes.medium
        ) {
            Text("Regresar a Home", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}