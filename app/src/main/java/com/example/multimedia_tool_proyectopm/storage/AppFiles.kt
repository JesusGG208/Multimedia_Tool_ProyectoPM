package com.example.multimedia_tool_proyectopm.storage

import android.content.Context
import com.example.multimedia_tool_proyectopm.media.AudioItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppFiles {

    fun audioFile(context: Context): File {
        val fecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "grabacion_${fecha}.m4a"
        return File(context.filesDir, fileName)
    }

    fun getSavedAudios(context: Context): List<AudioItem> {
        return context.filesDir.listFiles()
            ?.filter { it.extension == "m4a" }
            ?.map {
                AudioItem(
                    id = it.name,
                    nombre = it.name,
                    ruta = it.absolutePath,
                    fechaCreacion = it.lastModified()
                )
            }
            ?.sortedByDescending { it.fechaCreacion }
            ?: emptyList()
    }

    fun createImageFile(context: Context): File {
        val fecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "imagen_${fecha}.jpg"
        return File(context.filesDir, fileName)
    }

    fun processedPngFile(context: Context): File {

        return File(
            context.filesDir,
            "processed_image.png"
        )
    }

    fun processedJpgFile(context: Context): File {

        return File(
            context.filesDir,
            "processed_image.jpg"
        )
    }
}