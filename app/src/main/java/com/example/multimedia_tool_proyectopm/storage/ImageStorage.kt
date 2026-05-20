package com.example.multimedia_tool_proyectopm.storage


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageStorage {

    fun readBitmap(imageFile: File): Bitmap? {

        if (!imageFile.exists()) {
            return null
        }

        return BitmapFactory.decodeFile(
            imageFile.absolutePath
        )
    }

    fun saveBitmap(
        bitmap: Bitmap,
        outputFile: File,
        format: Bitmap.CompressFormat
    ) {

        try {

            FileOutputStream(outputFile).use { stream ->

                bitmap.compress(
                    format,
                    95,
                    stream
                )
            }

        } catch (_: Exception) {

        }
    }
}