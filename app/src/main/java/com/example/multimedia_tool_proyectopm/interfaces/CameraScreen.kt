package com.example.multimedia_tool_proyectopm.interfaces

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.multimedia_tool_proyectopm.storage.AppFiles

@Composable
fun CameraScreen(navController: NavHostController) {

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current

    val (permissionGranted, requestPermission) = rememberPhotoPermissionState()

    var captureUseCase by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var lastSavedPhoto by remember {
        mutableStateOf("Sin imágenes")
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
            Text("Volver")
        }

        Text(
            text = if (permissionGranted)
                "Permiso de cámara concedido"
            else
                "Permiso de cámara pendiente"
        )

        Text("Última captura: $lastSavedPhoto")

        if (!permissionGranted) {
            Button(onClick = requestPermission) {
                Text("Pedir permiso")
            }
        } else {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AndroidView(
                    factory = { ctx ->

                        val previewView = PreviewView(ctx)

                        val cameraProviderFuture =
                            ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({

                            val provider = cameraProviderFuture.get()

                            val previewUseCase = Preview.Builder().build()
                            previewUseCase.setSurfaceProvider(previewView.surfaceProvider)

                            captureUseCase = ImageCapture.Builder().build()

                            try {
                                provider.unbindAll()

                                provider.bindToLifecycle(
                                    lifecycle,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    previewUseCase,
                                    captureUseCase
                                )

                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error iniciando cámara",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    }
                )
            }

            Button(onClick = {

                val imageCapture = captureUseCase ?: return@Button

                val outputFile = AppFiles.createImageFile(context)

                val options =
                    ImageCapture.OutputFileOptions.Builder(outputFile).build()

                imageCapture.takePicture(
                    options,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            lastSavedPhoto = outputFile.name
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(
                                context,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

            }) {
                Text("Capturar foto")
            }
        }
    }
}
