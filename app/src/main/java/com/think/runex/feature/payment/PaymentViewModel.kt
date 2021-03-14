package com.think.runex.feature.payment

import com.think.runex.base.BaseViewModel
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

    fun updateOrderDetails(eventName: String, eventCode: String, registerId: String, orderId: String, price: Double) {
        this.eventName = eventName
        this.eventCode = eventCode
        this.registerId = registerId
        this.orderId = orderId
        this.price = price
    }

    suspend fun payEvent(omiseTokenId: String): Boolean = withContext(IO) {
        val body = PayEventBody().apply {
            this.omiseTokenId = omiseTokenId
            this.price = this@PaymentViewModel.price + (paymentMethod?.getChargeAmount(this@PaymentViewModel.price) ?: 0.0)
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
}