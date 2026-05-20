package com.example.multimedia_tool_proyectopm.interfaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


object Routes{
    const val HOME = "home"
    const val AUDIO_LIST = "audio_list"
    const val AUDIO = "AUDIO"
}

@Composable
fun AppNav(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.AUDIO_LIST) { AudioListScreen(navController) }

        composable(
            route = "${Routes.AUDIO}?fileName={fileName}",
            arguments = listOf(
                navArgument("fileName") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName")

            AudioScreen(navController = navController, fileName = fileName)
        }
    }
}