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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import com.example.multimedia_tool_proyectopm.storage.ImageStorage

@Composable
fun PhotoScreen(navController: NavHostController) {

    val context = LocalContext.current

    var info by remember {
        mutableStateOf("Esperando")
    }

    var originalBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var resultBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var convertToPng by remember {
        mutableStateOf(false)
    }

    var currentFileName by remember {
        mutableStateOf("Sin imagen")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Procesamiento de imágenes",
            fontWeight = FontWeight.Bold
        )

        Text("Estado: $info")

        Text("Archivo actual: $currentFileName")

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = {

                    val folder = context.filesDir

                    val latestImage =
                        folder.listFiles()
                            ?.filter {
                                it.extension == "jpg"
                            }
                            ?.maxByOrNull {
                                it.lastModified()
                            }

                    if (latestImage == null) {

                        info = "No hay imágenes"

                        return@Button
                    }

                    currentFileName = latestImage.name

                    originalBitmap =
                        BitmapFactory.decodeFile(
                            latestImage.absolutePath
                        )

                    resultBitmap = null

                    info = "Imagen cargada"
                }
            ) {
                Text("Cargar foto")
            }

            Button(
                onClick = {

                    val source = originalBitmap

                    if (source == null) {

                        info = "Carga una imagen primero"

                        return@Button
                    }

                    val stream =
                        java.io.ByteArrayOutputStream()

                    val format =
                        if (convertToPng)
                            Bitmap.CompressFormat.PNG
                        else
                            Bitmap.CompressFormat.JPEG

                    source.compress(
                        format,
                        100,
                        stream
                    )

                    val bytes = stream.toByteArray()

                    resultBitmap =
                        BitmapFactory.decodeByteArray(
                            bytes,
                            0,
                            bytes.size
                        )

                    info =
                        if (convertToPng)
                            "Convertida a PNG"
                        else
                            "Convertida a JPG"

                    convertToPng = !convertToPng
                }
            ) {

                Text(
                    if (convertToPng)
                        "Convertir PNG"
                    else
                        "Convertir JPG"
                )
            }
        }

        Button(
            onClick = {

                val image = resultBitmap

                if (image == null) {

                    info = "No hay imagen convertida"

                    return@Button
                }

                val outputFile =
                    if (convertToPng)
                        AppFiles.processedPngFile(context)
                    else
                        AppFiles.processedJpgFile(context)

                val format =
                    if (convertToPng)
                        Bitmap.CompressFormat.PNG
                    else
                        Bitmap.CompressFormat.JPEG

                val success =
                    ImageStorage.saveBitmap(
                        image,
                        outputFile,
                        format
                    )

                info =
                    if (success)
                        "Guardada: ${outputFile.name}"
                    else
                        "Error al guardar"
            }
        ) {

            Text("Guardar imagen")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            originalBitmap?.let { bitmap ->

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Original",
                        fontWeight = FontWeight.SemiBold
                    )

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            resultBitmap?.let { bitmap ->

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            if (convertToPng)
                                "Resultado JPG"
                            else
                                "Resultado PNG",

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

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Volver")
        }
    }
}