package com.example.mawi_app_back.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home    : BottomNavItem("home",    "Inicio",  Icons.Default.Home)
    data object Tasks   : BottomNavItem("tasks",   "Status",  Icons.Default.List)
    data object Users : BottomNavItem("users", "Usuarios",  Icons.Default.Person)
}
