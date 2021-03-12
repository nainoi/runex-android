package com.think.runex.feature.payment

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.payment.data.PaymentMethod

class PaymentRepository(private val api: PaymentApi) : RemoteDataSource() {

    suspend fun getPaymentMethods(): Result<List<PaymentMethod>> = call(api.getPaymentMethodsAsync())
}