package com.example.multimedia_tool_proyectopm.interfaces

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.multimedia_tool_proyectopm.storage.AppFiles
import java.io.File

@Composable
fun CameraScreen() {

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current

    val (cameraGranted, requestPermission) =
        rememberPhotoPermissionState()

    var cameraState by remember {
        mutableStateOf("Preparada")
    }

    var currentPhoto by remember {
        mutableStateOf("Sin capturas")
    }

    val imageCapture = remember {
        mutableStateOf<ImageCapture?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text("Captura de imágenes")

        Text(
            if (cameraGranted)
                "Permiso concedido"
            else
                "Permiso denegado"
        )

        Text("Estado: $cameraState")

        Text("Última imagen: $currentPhoto")

        if (!cameraGranted) {

            Button(
                onClick = requestPermission
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

                val providerFuture =
                    ProcessCameraProvider.getInstance(ctx)

                providerFuture.addListener({

                    val provider = providerFuture.get()

                    val previewUseCase =
                        Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(
                                    previewView.surfaceProvider
                                )
                            }

                    val captureUseCase =
                        ImageCapture.Builder()
                            .setCaptureMode(
                                ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                            )
                            .build()

                    imageCapture.value = captureUseCase

                    try {

                        provider.unbindAll()

                        provider.bindToLifecycle(
                            lifecycle,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            previewUseCase,
                            captureUseCase
                        )

                        cameraState = "Cámara iniciada"

                    } catch (e: Exception) {

                        cameraState = "Error al iniciar"

                        Log.e(
                            "CameraScreen",
                            e.message ?: "Error cámara"
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

                    cameraState = "La cámara aún no está lista"

                    return@Button
                }

                val imageFile: File =
                    AppFiles.latestPhotoFile(context)

                imageFile.parentFile?.mkdirs()

                val output =
                    ImageCapture.OutputFileOptions.Builder(imageFile)
                        .build()

                cameraState = "Realizando captura..."

                capture.takePicture(
                    output,
                    ContextCompat.getMainExecutor(context),

                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(
                            outputFileResults: ImageCapture.OutputFileResults
                        ) {

                            currentPhoto = imageFile.name

                            cameraState = "Imagen guardada"
                        }

                        override fun onError(
                            exception: ImageCaptureException
                        ) {

                            cameraState =
                                "Error: ${exception.message}"
                        }
                    }
                )
            }
        ) {
            Text("Capturar foto")
        }
    }
}