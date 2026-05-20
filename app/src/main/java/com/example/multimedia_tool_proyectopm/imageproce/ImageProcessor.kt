package com.example.multimedia_tool_proyectopm.imageproce

import android.graphics.Bitmap
import androidx.core.graphics.get
import androidx.core.graphics.set

interface ImageProcessor{
    val name: String
    fun apply(src: Bitmap): Bitmap
}

class GrayscaleProcessor: ImageProcessor {
    override val name = "Gris"
    override fun apply(src: Bitmap): Bitmap {
        val w = src.width
        val h = src.height

        val out = src.copy(Bitmap.Config.ARGB_8888, true)

        for (y in 0 until h){
            for (x in 0 until w){
                val c = out.get(x,y)
                val r = (c shr 16) and 0xFF
                val g = (c shr 8) and 0XFF
                val b = c and 0XFF
                val a = (c ushr 24) and 0XFF
                val gray = (0.299 * r + 0.587 * g + 0.144 * b).toInt()
                val newC = (0.299 * r + 0.587 * g + 0.144 * b).toInt()
                out[x, y] = newC
            }
            }
        return out
    }
}