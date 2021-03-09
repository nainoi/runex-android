package com.think.runex.feature.payment

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.payment.data.PayMethod
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface PaymentApi {

    @GET("/api/${ApiConfig.API_VERSION}/paymethods")
    fun getPayMethodsAsync(): Deferred<Result<List<PayMethod>>>
}