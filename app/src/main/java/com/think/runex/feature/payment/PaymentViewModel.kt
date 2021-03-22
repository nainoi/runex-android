package com.think.runex.feature.payment

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.think.runex.base.BaseViewModel
import com.think.runex.config.DEFAULT_QR_CODE_SIZE
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.payment.data.request.PayEventBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class PaymentViewModel(private val repo: PaymentRepository) : BaseViewModel() {

    var eventName: String = ""
        private set

    var eventCode: String = ""
        private set

    var registerId: String = ""
        private set

    var orderId: String = ""
        private set

    var ref2: String = ""
        private set

    var price: Double = 0.0
        private set

    var paymentMethod: PaymentMethod? = null

    suspend fun getPaymentMethods(): List<PaymentMethod>? = withContext(IO) {
        val result = repo.getPaymentMethods()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    fun updateOrderDetails(eventName: String, eventCode: String, registerId: String, orderId: String, ref2: String, price: Double) {
        this.eventName = eventName
        this.eventCode = eventCode
        this.registerId = registerId
        this.orderId = orderId
        this.ref2 = ref2
        this.price = price
    }

    suspend fun payEventByCreditOrDebitCard(omiseTokenId: String): Boolean = withContext(IO) {
        val body = PayEventBody().apply {
            this.omiseTokenId = omiseTokenId
            this.price = this@PaymentViewModel.price + (paymentMethod?.getChargeAmount(this@PaymentViewModel.price)
                    ?: 0.0)
            this.eventCode = this@PaymentViewModel.eventCode
            this.registerId = this@PaymentViewModel.registerId
            this.orderId = this@PaymentViewModel.orderId
        }

        val result = repo.payEvent(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.isSuccessful()
    }

    suspend fun generateQRData(context: Context): Bitmap? = withContext(IO) {
        val totalPrice = price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
        val url = "${ApiConfig.QR_URL}/$orderId/$ref2/$totalPrice"

        val result = repo.generateQRCodeData(url)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
            return@withContext null
        }
        return@withContext convertStringToQrCode(context, result.data ?: "")
    }

    suspend fun generateQRCodeData(context: Context) = withContext(IO) {
        val totalPrice = price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
        val url = "${ApiConfig.QR_CODE_URL}/$orderId/$ref2/$totalPrice"

        val result = repo.generateQRCodeData(url)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
            return@withContext null
        }
        return@withContext convertStringToQrCode(context, result.data ?: "")
    }

    private fun convertStringToQrCode(context: Context, data: String): Bitmap? {
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

    fun getTotalPrice(): Double {
        return price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
    }
}