package com.think.runex.feature.qr

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.think.runex.config.DEFAULT_QR_CODE_SIZE

class QRCodeUtil {

    fun generateQRCode(context: Context, data: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE,
                DEFAULT_QR_CODE_SIZE, DEFAULT_QR_CODE_SIZE, null)

        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = ContextCompat.getColor(context, when (bitMatrix.get(x, y)) {
                    true -> android.R.color.black
                    false -> android.R.color.white
                })
            }
        }

        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, bitMatrixWidth, 0, 0, bitMatrixWidth, bitMatrixHeight)

        return bitmap
    }
}