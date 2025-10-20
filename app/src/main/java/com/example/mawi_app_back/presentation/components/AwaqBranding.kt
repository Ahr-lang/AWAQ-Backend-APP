package com.example.mawi_app_back.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mawi_app_back.R

/**
 * Logo y título de AWAQ
 */
@Composable
fun AwaqLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.awaq),
            contentDescription = "Logo Awaq",
            modifier = Modifier
                .height(96.dp)
                .padding(bottom = if (showText) 8.dp else 0.dp)
        )
        if (showText) {
            Text(
                text = "AWAQ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )
        }
    }
}

/**
 * Header de bienvenida con logo y textos
 */
@Composable
fun AwaqHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val ink = Color(0xFF111111)
    val subtleText = Color(0xFF50565A)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AwaqLogo()

        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = ink,
            letterSpacing = 0.2.sp
        )
        Text(
            text = subtitle,
            color = subtleText,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )
    }
}
