package com.think.runex.feature.payment.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.util.Base64
import com.google.gson.annotations.SerializedName
import java.nio.ByteBuffer

data class QRCodeImage(
        @SerializedName("qrRawData") var qrRawData: String? = null,
        @SerializedName("qrImage") var qrImage: String? = null) {

    fun getQRCodeImage(): Bitmap? {

        val imageBytes = Base64.decode(qrImage, Base64.DEFAULT)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            ImageDecoder.decodeBitmap(ImageDecoder.createSource(ByteBuffer.wrap(imageBytes)))
//        } else {
//            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        }
    }
}