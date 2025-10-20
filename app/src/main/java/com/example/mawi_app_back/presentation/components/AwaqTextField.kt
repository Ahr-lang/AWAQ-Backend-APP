package com.example.mawi_app_back.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mawi_app_back.ui.theme.AwaqGreen

/**
 * TextField genérico con estilo AWAQ
 */
@Composable
fun AwaqTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    val ink = Color(0xFF111111)
    val fieldBorder = Color(0xFFCBD5D1)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = label,
                    tint = AwaqGreen
                )
            }
        },
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        enabled = enabled,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AwaqGreen,
            unfocusedBorderColor = fieldBorder,
            focusedLabelColor = AwaqGreen,
            cursorColor = AwaqGreen,
            focusedLeadingIconColor = AwaqGreen,
            unfocusedLeadingIconColor = AwaqGreen.copy(alpha = 0.9f),
            focusedTextColor = ink,
            unfocusedTextColor = ink,
            disabledBorderColor = fieldBorder.copy(alpha = 0.6f),
            disabledLabelColor = fieldBorder,
            disabledTextColor = ink.copy(alpha = 0.6f)
        )
    )
}

/**
 * TextField específico para email
 */
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Correo electrónico",
    enabled: Boolean = true
) {
    AwaqTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Outlined.Email,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = modifier,
        enabled = enabled
    )
}

/**
 * TextField específico para contraseña con toggle de visibilidad
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    AwaqTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Outlined.Lock,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = AwaqGreen
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier,
        enabled = enabled
    )
}
