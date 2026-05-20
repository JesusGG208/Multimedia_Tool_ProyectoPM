package com.example.multimedia_tool_proyectopm.interfaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import com.example.multimedia_tool_proyectopm.storage.ImageStorage

@Composable
fun PhotoScreen() {

    val context = LocalContext.current

    val photoFile = AppFiles.latestPhotoFile(context)

    var exists by remember {
        mutableStateOf(photoFile.exists())
    }

    var message by remember {
        mutableStateOf("Esperando acción")
    }

    var sourceBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var convertedBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var convertToPng by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Procesamiento de imágenes",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Archivo: ${photoFile.name}"
        )

        Text(
            text = if (exists)
                "Imagen encontrada"
            else
                "No existe imagen"
        )

        Text("Estado actual: $message")

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = {

                    sourceBitmap = ImageStorage.readBitmap(photoFile)

                    convertedBitmap = null

                    message = if (sourceBitmap != null)
                        "Imagen cargada correctamente"
                    else
                        "Error al cargar imagen"
                }
            ) {
                Text("Abrir imagen")
            }

            Button(
                onClick = {

                    val original = sourceBitmap

                    if (original == null) {
                        message = "Debes cargar una imagen"
                        return@Button
                    }

                    val memoryStream = java.io.ByteArrayOutputStream()

                    val outputFormat =
                        if (convertToPng)
                            Bitmap.CompressFormat.PNG
                        else
                            Bitmap.CompressFormat.JPEG

                    original.compress(
                        outputFormat,
                        100,
                        memoryStream
                    )

                    val bytes = memoryStream.toByteArray()

                    convertedBitmap = BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )

                    message =
                        if (convertToPng)
                            "Convertida a PNG"
                        else
                            "Convertida a JPG"

                    convertToPng = !convertToPng
                }
            ) {
                Text(
                    if (convertToPng)
                        "Pasar a PNG"
                    else
                        "Pasar a JPG"
                )
            }
        }

        Button(
            onClick = {

                val imageToSave = convertedBitmap

                if (imageToSave == null) {
                    message = "No hay imagen para guardar"
                    return@Button
                }

                val destination =
                    if (convertToPng)
                        AppFiles.processedPngFile(context)
                    else
                        AppFiles.processedJpgFile(context)

                val format =
                    if (convertToPng)
                        Bitmap.CompressFormat.PNG
                    else
                        Bitmap.CompressFormat.JPEG

                val saved = ImageStorage.saveBitmap(
                    imageToSave,
                    destination,
                    format
                )

                message =
                    if (saved)
                        "Guardada: ${destination.name}"
                    else
                        "No se pudo guardar"
            }
        ) {
            Text("Guardar resultado")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            sourceBitmap?.let { bitmap ->

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        "Original",
                        fontWeight = FontWeight.SemiBold
                    )

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            convertedBitmap?.let { bitmap ->

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            if (convertToPng)
                                "Formato JPG"
                            else
                                "Formato PNG",
                        fontWeight = FontWeight.SemiBold
                    )

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}