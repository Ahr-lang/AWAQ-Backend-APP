package com.example.mawi_app_back.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.presentation.components.*
import com.example.mawi_app_back.ui.theme.AwaqGreen
import com.example.mawi_app_back.ui.theme.White

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
) {
    // Paleta base consistente
    val Ink = Color(0xFF111111)
    val SubtleText = Color(0xFF50565A)
    val DividerSoft = Color(0xFFE6E8EA)

    AwaqBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo AWAQ
            AwaqLogo()

            Spacer(Modifier.height(24.dp))

            // Card principal
            AwaqSimpleCard {
                Text(
                    text = "Inicio",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Ink
                )

                Spacer(Modifier.height(20.dp))

                Divider(color = DividerSoft)

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onLogout) {
                    Text(
                        "Cerrar sesión",
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}