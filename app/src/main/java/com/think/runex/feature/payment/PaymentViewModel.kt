package com.think.runex.feature.payment

import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.payment.data.PaymentMethod
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class PaymentViewModel(private val repo: PaymentRepository) : BaseViewModel() {


    var eventName: String = ""
        private set
    var orderId: String = ""
        private set
    var price: Double = 0.0
        private set

    suspend fun getPaymentMethods(): List<PaymentMethod>? = withContext(IO) {
        val result = repo.getPaymentMethods()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    fun updateOrderDetails(eventName: String, orderId: String, price: Double) {
        this.eventName = eventName
        this.orderId = orderId
        this.price = price
    }
}