package com.example.mawi_app_back.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.ui.theme.AwaqGreen

/**
 * Indicador de carga centrado
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    message: String = "Cargando..."
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = AwaqGreen)
        if (message.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                color = Color(0xFF50565A),
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Mensaje de error estilizado
 */
@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️ Error",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            if (onRetry != null) {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
        }
    }
}

/**
 * Overlay de carga animado
 */
@Composable
fun LoadingOverlay(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AwaqGreen)
        }
    }
}

/**
 * Mensaje cuando no hay datos
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: String = "📭"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 48.sp
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            color = Color(0xFF50565A),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
