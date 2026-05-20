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
    const val AUDIO = "audio"
    const val IMAGE = "image"
    const val CAMERA = "camera"
    const val VIDEO = "video"
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

        composable(Routes.IMAGE) { PhotoScreen() }
        composable(Routes.CAMERA) { CameraScreen() }
        composable(Routes.VIDEO) { VideoPlayerScreen(navController) }
    }
}