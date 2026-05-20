package com.example.multimedia_tool_proyectopm.interfaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Herramienta multimedia"
        )

        Button(
            onClick = {
                navController.navigate(Routes.AUDIO_LIST)
            }
        ) {
            Text("Grabadora de audio")
        }
        Button(
            onClick = {
                navController.navigate(Routes.IMAGE)
            }
        ) {
            Text("Imagen")
        }
        Button(
            onClick = {
                navController.navigate(Routes.CAMERA)
            }
        ) {
            Text("Cámara")
        }
        Button(
            onClick = {
                navController.navigate(Routes.VIDEO)
            }
        ) {
            Text("Reproductor de vídeo")
        }
    }
}