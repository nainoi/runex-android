package com.think.runex.feature.payment

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.payment.data.request.PayEventBody

class PaymentRepository(private val api: PaymentApi) : RemoteDataSource() {

    suspend fun getPaymentMethods(): Result<List<PaymentMethod>> = call(api.getPaymentMethodsAsync())

    suspend fun payEvent(body: PayEventBody): Result<Any> = call(api.payEventAsync(body))
}