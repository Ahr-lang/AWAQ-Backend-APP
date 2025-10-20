package com.example.mawi_app_back.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.ui.theme.AwaqGreen
import com.example.mawi_app_back.ui.theme.White

/**
 * Botón primario con estilo AWAQ
 */
@Composable
fun AwaqPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AwaqGreen,
            contentColor = White,
            disabledContainerColor = AwaqGreen.copy(alpha = 0.5f),
            disabledContentColor = White.copy(alpha = 0.7f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = White,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Botón secundario (outline) con estilo AWAQ
 */
@Composable
fun AwaqSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = AwaqGreen
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )
    }
}
