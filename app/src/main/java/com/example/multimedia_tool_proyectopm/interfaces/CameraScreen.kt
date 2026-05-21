package com.example.multimedia_tool_proyectopm.interfaces

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import java.io.File
import java.util.concurrent.Executors

@Composable
fun CameraScreen(navController: NavHostController) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val (hasPermission, requestPermission) =
        rememberPhotoPermissionState()

    var cameraStatus by remember {
        mutableStateOf("Inicializando")
    }

    var lastImage by remember {
        mutableStateOf("Sin imágenes")
    }

    val imageCapture = remember {
        mutableStateOf<ImageCapture?>(null)
    }

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Pantalla de cámara")

        Text(
            if (hasPermission)
                "Permiso concedido"
            else
                "Permiso no concedido"
        )

        Text("Estado: $cameraStatus")

        Text("Última imagen: $lastImage")

        if (!hasPermission) {

            Button(
                onClick = {
                    requestPermission()
                }
            ) {
                Text("Solicitar permiso")
            }

            return@Column
        }

        AndroidView(

            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            factory = { ctx ->

                val previewView = PreviewView(ctx)

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({

                    try {

                        val cameraProvider =
                            cameraProviderFuture.get()

                        val preview =
                            Preview.Builder()
                                .build()

                        preview.surfaceProvider =
                            previewView.surfaceProvider

                        val capture =
                            ImageCapture.Builder()
                                .setCaptureMode(
                                    ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                                )
                                .build()

                        imageCapture.value = capture

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            capture
                        )

                        cameraStatus = "Cámara preparada"

                    } catch (e: Exception) {

                        cameraStatus =
                            "Error cámara"

                        Log.e(
                            "CAMERA",
                            "ERROR INIT",
                            e
                        )
                    }

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        Button(

            onClick = {

                val capture = imageCapture.value

                if (capture == null) {

                    cameraStatus =
                        "Captura no disponible"

                    return@Button
                }

                val photoFile: File =
                    AppFiles.createImageFile(context)

                val outputOptions =
                    ImageCapture.OutputFileOptions.Builder(photoFile)
                        .build()

                cameraStatus = "Capturando..."

                capture.takePicture(

                    outputOptions,

                    cameraExecutor,

                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(
                            outputFileResults: ImageCapture.OutputFileResults
                        ) {

                            lastImage = photoFile.name

                            cameraStatus =
                                "Imagen guardada"

                            Log.d(
                                "CAMERA",
                                "Guardada correctamente"
                            )
                        }

                        override fun onError(
                            exception: ImageCaptureException
                        ) {

                            cameraStatus =
                                "Error: ${exception.message}"

                            Log.e(
                                "CAMERA",
                                "ERROR FOTO",
                                exception
                            )
                        }
                    }
                )
            }
        ) {

            Text("Hacer foto")
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