package com.think.runex.feature.payment

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.payment.data.PayMethod

class PaymentRepository(private val api: PaymentApi) : RemoteDataSource() {

    suspend fun getPayMethods(): Result<List<PayMethod>> = call(api.getPayMethodsAsync())
}