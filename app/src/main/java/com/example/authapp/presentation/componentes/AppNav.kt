package com.example.authapp.presentation.componentes

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.core.net.toUri

import com.example.authapp.presentation.mainViews.HomeScreen

object Routes {
    const val HOME = "mainScreen"
}

@Composable
fun AppNav( // en instacam era viewModel: GalleryViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

    }
}