package com.example.multimedia_tool_proyectopm.interfaces

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

@Composable
fun VideoScreen(navController: NavController) {

    val context = LocalContext.current

    val videoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    var currentVideo by remember {
        mutableStateOf<Uri?>(null)
    }

    var playbackText by remember {
        mutableStateOf("Ningún vídeo seleccionado")
    }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        currentVideo = uri

        uri?.let {

            playbackText = "Vídeo cargado"

            val media = MediaItem.fromUri(it)

            videoPlayer.setMediaItem(media)

            videoPlayer.prepare()
        }
    }

    DisposableEffect(Unit) {

        onDispose {
            videoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Regresar")
        }

        Text(playbackText)

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            AndroidView(
                factory = {

                    PlayerView(it).apply {
                        player = videoPlayer
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(onClick = {
                videoPicker.launch("video/*")
            }) {
                Text("Abrir vídeo")
            }

            Button(onClick = {
                videoPlayer.play()
                playbackText = "Reproduciendo"
            }) {
                Text("Play")
            }

            Button(onClick = {
                videoPlayer.pause()
                playbackText = "Pausado"
            }) {
                Text("Pause")
            }
        }
    }
}