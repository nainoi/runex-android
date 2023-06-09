package com.think.runex.feature.payment

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.payment.data.QRCodeImage
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.http.*

interface PaymentApi {

    @GET("/api/${ApiConfig.API_VERSION}/paymethods")
    fun getPaymentMethodsAsync(): Deferred<Result<List<PaymentMethod>>>

    @POST("/api/${ApiConfig.API_VERSION}/register/payment")
    fun payEventAsync(@Body body: RequestBody): Deferred<Result<Any>>

    @GET
    fun getQRCodeImageAsync(@Url url: String): Deferred<Result<QRCodeImage>>
}