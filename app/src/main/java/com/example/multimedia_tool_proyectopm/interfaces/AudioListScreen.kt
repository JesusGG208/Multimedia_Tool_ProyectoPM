package com.example.multimedia_tool_proyectopm.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AudioListScreen(navController: NavHostController) {

    val context = LocalContext.current
    val audioList = remember {
        AppFiles.getSavedAudios(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Grabadora de audio",
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                navController.navigate(Routes.AUDIO)
            }
        ) {
            Text("Nueva grabación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista de audios",
            modifier = Modifier.padding(16.dp)
        )

        if (audioList.isEmpty()) {
            Text("No hay audios guardados")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(audioList) { audio ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("${Routes.AUDIO}?fileName=${audio.nombre}")
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Nombre: ${audio.nombre}")
                            Text("Fecha: ${formatDate(audio.fechaCreacion)}")
                        }
                    }
                }
            }
        }
    }
}

fun formatDate(time: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(time))
}
