package com.think.runex.feature.payment

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.config.KEY_QR
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.qr.QRCodeUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

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

    private var qrCodeImage: Bitmap? = null

    suspend fun getPaymentMethods(): List<PaymentMethod>? = withContext(IO) {
        val result = repo.getPaymentMethods()
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }
        return@withContext result.data
    }

    fun updateOrderDetails(eventCode: String, eventName: String, orderId: String, registerId: String, ref2: String, price: Double) {
        this.eventName = eventName
        this.eventCode = eventCode
        this.registerId = registerId
        this.orderId = orderId
        this.ref2 = ref2
        this.price = price
    }

    suspend fun payEventByCreditOrDebitCard(omiseTokenId: String): Boolean = withContext(IO) {

        val price = price + (paymentMethod?.getChargeAmount(price) ?: 0.0)

        val result = repo.payEvent(omiseTokenId, price, eventCode, registerId, orderId)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }
        return@withContext result.isSuccessful()
    }

    suspend fun generateQRImage(context: Context): Bitmap? = withContext(IO) {
        val totalPrice = price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
        val url = "${ApiConfig.QR_URL}/$orderId/$ref2/$totalPrice"

        val result = repo.getQRData(url)
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
            return@withContext null
        }
        qrCodeImage = QRCodeUtil().generateQRCode(context, result.data ?: "")
        return@withContext qrCodeImage
    }

    suspend fun generateQRCodeImage(): Bitmap? = withContext(IO) {
        val totalPrice = price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
        val url = "${ApiConfig.QR_CODE_URL}/$orderId/$ref2/$totalPrice"

        val result = repo.getQRCodeImage(url)
        if (result.data == null) {
            onHandleError(HttpURLConnection.HTTP_BAD_REQUEST, "The service is unavailable for now.", KEY_QR)
            return@withContext null
        }
        qrCodeImage = result.data?.getQRCodeImage()
        return@withContext qrCodeImage
    }


    fun getTotalPrice(): Double {
        return price + (paymentMethod?.getChargeAmount(price) ?: 0.0)
    }

    override fun onCleared() {
        qrCodeImage?.recycle()
        super.onCleared()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PaymentViewModel(PaymentRepository(ApiService().provideService(context, PaymentApi::class.java))) as T
        }
    }
}