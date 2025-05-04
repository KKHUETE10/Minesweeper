package com.example.minesweeper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Inicio", Icons.Filled.Home)
    object Help : BottomNavItem("help", "Ayuda", Icons.Filled.Help)
    object Settings : BottomNavItem("settings", "Configuración", Icons.Filled.Settings)
}