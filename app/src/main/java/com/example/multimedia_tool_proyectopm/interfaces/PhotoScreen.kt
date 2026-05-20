package com.example.multimedia_tool_proyectopm.interfaces

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import com.example.multimedia_tool_proyectopm.storage.Bit_Map

@Composable
fun PhotoScreen(navController: NavController) {

    val context = LocalContext.current

    var images by remember {
        mutableStateOf(
            AppFiles.getImages(context)
        )
    }

    var currentPosition by remember {
        mutableStateOf(0)
    }

    val selectedImage = images.getOrNull(currentPosition)

    var saveMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Inicio")
            }

            Button(onClick = {
                navController.navigate("camera")
            }) {
                Text("Abrir cámara")
            }
        }

        Text("Galería de imágenes")

        selectedImage?.let { file ->

            val bitmap = Bit_Map.readBitmap(file)

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )
            }

            Text(file.name)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Button(onClick = {
                    currentPosition =
                        if (currentPosition <= 0)
                            images.size - 1
                        else
                            currentPosition - 1
                }) {
                    Text("Anterior")
                }

                Button(onClick = {
                    currentPosition =
                        if (currentPosition >= images.size - 1)
                            0
                        else
                            currentPosition + 1
                }) {
                    Text("Siguiente")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Button(onClick = {

                    val newFile =
                        AppFiles.changeExtension(file, "png")

                    Bit_Map.saveBitmap(
                        bitmap,
                        newFile,
                        Bitmap.CompressFormat.PNG
                    )

                    saveMessage = "Imagen PNG creada"

                    images = AppFiles.getImages(context)

                }) {
                    Text("Guardar PNG")
                }

                Button(onClick = {

                    val newFile =
                        AppFiles.changeExtension(file, "jpg")

                    Bit_Map.saveBitmap(
                        bitmap,
                        newFile,
                        Bitmap.CompressFormat.JPEG
                    )

                    saveMessage = "Imagen JPG creada"

                    images = AppFiles.getImages(context)

                }) {
                    Text("Guardar JPG")
                }
            }

            Text(saveMessage)
        }
    }
}
