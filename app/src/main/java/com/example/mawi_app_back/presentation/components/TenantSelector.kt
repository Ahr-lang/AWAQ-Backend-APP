package com.example.mawi_app_back.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Selector de tenants con botones
 */
@Composable
fun TenantSelector(
    tenants: List<String>,
    currentTenant: String,
    onTenantSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tenants.forEach { tenant ->
            Button(
                onClick = { onTenantSelected(tenant) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (tenant == currentTenant)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(tenant)
            }
        }
    }
}
