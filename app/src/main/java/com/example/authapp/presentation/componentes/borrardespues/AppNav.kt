package com.example.authapp.presentation.componentes.borrardespues

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.authapp.presentation.users.UsersScreen

object Routes {
    // const val HOME = "HomeScreen"
    const val USER = "VistaUsuarios"
}

@Composable
fun AppNav( // en instacam era viewModel: GalleryViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.USER
    ) {
        // CHECAR ESTA SECCION, NO ESTOY SEGURO COMO IMPLEMENTAR BIEN
//        composable(Routes.HOME) {
//            HomeScreen(
//                onLogout,
//            onNavigateToForm:
//            )
//        }

        composable(Routes.USER) {
            UsersScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}