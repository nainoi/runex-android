package com.think.runex.common

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorInt
import java.io.ByteArrayOutputStream
import java.io.OutputStream

fun Drawable.setColorFilter(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        this.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun Bitmap.toByteArray(): ByteArray = try {
    ByteArrayOutputStream().use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
        return@use it.toByteArray()
    }
} catch (throwable: Throwable) {
    throwable.printStackTrace()
    ByteArray(0)
}

//fun Uri.toBitmap(context: Context) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//        ImageDecoder.createSource(context.contentResolver, this)
//    }
//}

fun Bitmap.writeToOutputStream(outputStream: OutputStream) {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
    outputStream.write(bos.toByteArray())
    outputStream.close()
}