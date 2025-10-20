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
            // Encabezado
            Text(
                text = "Bienvenido a AWAQ 🌿",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Gestiona tus tareas y recursos aquí",
                fontSize = 15.sp,
                color = SubtleText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Card principal
            AwaqSimpleCard {
                Text(
                    text = "Inicio",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Ink
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Explora tus módulos, revisa usuarios por tenant o continúa con tus tareas.",
                    color = SubtleText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                // Botones principales
                AwaqPrimaryButton(
                    text = "Ir a Usuarios",
                    onClick = { /* TODO: Navegar a Usuarios */ }
                )

                Spacer(Modifier.height(12.dp))

                AwaqSecondaryButton(
                    text = "Ver Tareas",
                    onClick = { /* TODO: Navegar a Tareas */ }
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

            Spacer(Modifier.height(24.dp))

            // Card informativa
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAF8))
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        text = "Consejos",
                        fontWeight = FontWeight.SemiBold,
                        color = Ink
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "• Cambia el tenant desde la pestaña Usuarios para ver listas distintas.\n" +
                                "• Asegúrate de haber iniciado sesión para acceder a endpoints protegidos.\n" +
                                "• Mantén tu token seguro: se guarda localmente en DataStore.",
                        color = SubtleText,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}