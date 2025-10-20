package com.example.mawi_app_back.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.ui.theme.AwaqGreen

/**
 * Fondo con gradiente AWAQ estándar
 */
@Composable
fun AwaqBackground(
    modifier: Modifier = Modifier,
    paddingHorizontal: Int = 20,
    paddingVertical: Int = 16,
    content: @Composable BoxScope.() -> Unit
) {
    val greenSoft = AwaqGreen.copy(alpha = 0.08f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(greenSoft, Color.White)
                )
            )
            .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp)
    ) {
        content()
    }
}
