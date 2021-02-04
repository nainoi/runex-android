package com.think.runex.common

import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import java.io.ByteArrayOutputStream

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