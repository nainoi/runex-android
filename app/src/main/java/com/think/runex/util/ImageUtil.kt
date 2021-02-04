package com.think.runex.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.jozzee.android.core.util.Logger
import com.think.runex.common.toByteArray
import java.io.IOException
import java.io.InputStream

open class ImageUtil {
    /**
     * [maxWidthOrHeight] Default size to reduce
     */
    fun reduceImageSize(context: Context, uri: Uri, maxWidthOrHeight: Int = 1080): ByteArray {

        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
        return try {
            /**
             * Get size of image without loading into memory.
             */
            inputStream = context.contentResolver?.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            Logger.debug("ImageUtil", "Default bitmap size: ${options.outWidth}x${options.outHeight}")

            /**
             * Reducing image size if outWidth or outHeight more than [maxWidthOrHeight]
             * and update inSampleSize in bitmap options.
             */
            var reqWidth = 0
            var reqHeight = 0

            if (maxWidthOrHeight > 0 &&
                    (options.outWidth > maxWidthOrHeight || options.outHeight > maxWidthOrHeight)) {

                when {
                    options.outWidth >= options.outHeight -> {
                        reqWidth = maxWidthOrHeight
                        reqHeight = (options.outHeight / (options.outWidth / reqWidth.toFloat())).toInt()
                    }
                    options.outWidth < options.outHeight -> {
                        reqHeight = maxWidthOrHeight
                        reqWidth = (options.outWidth / (options.outHeight / reqHeight.toFloat())).toInt()
                    }
                    else -> {
                        reqWidth = options.outWidth
                        reqHeight = options.outHeight
                    }
                }
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            }

            /**
             * Decode [Bitmap] from [Uri] and bitmap options.
             */
            inputStream = context.contentResolver?.openInputStream(uri)
            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(inputStream, null, options)

            /**
             * Down scale size bitmap to [maxWidthOrHeight] if after decoded an size of bitmap has still more than [maxWidthOrHeight].
             */
            if (bitmap != null && ((bitmap.width > maxWidthOrHeight) || (bitmap.height > maxWidthOrHeight))) {
                val scaleWidth: Float = reqWidth.toFloat() / (bitmap.width)
                val scaleHeight: Float = reqHeight.toFloat() / (bitmap.height)
                val matrix = Matrix()
                matrix.postScale(scaleWidth, scaleHeight)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
                Logger.debug("ImageUtil", "New bitmap size: ${bitmap?.width}x${bitmap?.height}")
            }

            //Here's what the orientation values mean: http://sylvana.net/jpegcrop/exif_orientation.html
            inputStream = context.contentResolver?.openInputStream(uri)
            if (inputStream != null) {
                val exif = ExifInterface(inputStream)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                if (orientation != 1) {
                    val matrix = Matrix()
                    when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) {
                        3 -> matrix.postRotate(180f)
                        6 -> matrix.postRotate(90f)
                        8 -> matrix.postRotate(270f)
                    }
                    if (bitmap != null) {
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                        Logger.debug("ImageUtil", "Rotate Image: w: ${bitmap.width}, h: ${bitmap.height}")
                    }
                }
            }
            bitmap?.toByteArray() ?: ByteArray(0)

        } catch (e: IOException) {
            e.printStackTrace()
            ByteArray(0)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            ByteArray(0)
        } finally {
            inputStream?.close()
            bitmap?.recycle()
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}