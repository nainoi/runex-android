package com.think.runex.datasource.api

import com.think.runex.config.AUTHORIZATION
import com.think.runex.feature.auth.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
                .addHeader(AUTHORIZATION, TokenManager.accessToken)
                .build()
        return chain.proceed(newRequest)
    }
}