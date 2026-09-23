package com.tuapp.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuapp.navlab.screens.DetailScreen
import com.tuapp.navlab.screens.HomeScreen
import com.tuapp.navlab.screens.ListScreen
import com.tuapp.navlab.screens.ProfileScreen

@Composable
fun AppNavigation() {
    // rememberNavController() crea y mantiene el controlador
    val navController = rememberNavController()

    // NavHost como contenedor del grafo con destino inicial Screen.Home.route
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Screen.Home.route -> "home"
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Screen.List.route -> "list"
        composable(route = Screen.List.route) {
            ListScreen(navController = navController)
        }

        // Screen.Profile.route -> "profile"
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        // Screen.Detail.route -> "detail/{itemId}" (con argumento de tipo entero)
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(navController = navController, itemId = itemId)
        }
    }
}