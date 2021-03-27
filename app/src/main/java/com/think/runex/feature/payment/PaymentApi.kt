package com.think.runex.feature.payment

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.payment.data.QRCodeImage
import com.think.runex.feature.payment.data.request.PayEventBody
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface PaymentApi {

    @GET("/api/${ApiConfig.API_VERSION}/paymethods")
    fun getPaymentMethodsAsync(): Deferred<Result<List<PaymentMethod>>>

    @POST("/api/${ApiConfig.API_VERSION}/register/payment")
    fun payEventAsync(
            @Body body: PayEventBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @GET
    fun getQRCodeImageAsync(@Url url: String,
                            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<QRCodeImage>>
}