package com.think.runex.util.extension

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.ColorInt
import com.think.runex.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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

fun Bitmap.saveToExternalStorage(context: Context, fileName: String): Uri? {

    var uri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        val value = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.ImageColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${BuildConfig.APP_NAME}")
            put(MediaStore.Images.Media.IS_PENDING, true)
            //RELATIVE_PATH and IS_PENDING are introduced in API 29.
        }

        uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                this.writeToOutputStream(outputStream, Bitmap.CompressFormat.JPEG)
            }
            value.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, value, null, null)
        }

    } else {

        //getExternalStorageDirectory is deprecated in API 29
        val outputFile = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/${BuildConfig.APP_NAME}").let { directory ->
            if (directory.exists().not()) {
                directory.mkdirs()
            }
            File("$directory/$fileName")
        }
        uri = outputFile.getUriProvider(context)
        this.writeToOutputStream(FileOutputStream(outputFile), Bitmap.CompressFormat.JPEG)

        //Add image to gallery
        //context.sendBroadcast(Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    }

    return uri
}

fun Bitmap.writeToOutputStream(outputStream: OutputStream, format: Bitmap.CompressFormat) {
    val bos = ByteArrayOutputStream()
    this.compress(format, 100, bos)
    outputStream.write(bos.toByteArray())
    outputStream.close()
}