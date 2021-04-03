package com.think.runex.feature.payment

import com.jozzee.android.core.connection.NetworkMonitor
import com.think.runex.BuildConfig
import com.think.runex.config.ERR_NO_INTERNET_CONNECTION
import com.think.runex.config.ERR_NO_STATUS_CODE
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.payment.data.PaymentMethod
import com.think.runex.feature.payment.data.QRCodeImage
import com.think.runex.feature.payment.data.request.PayEventBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class PaymentRepository(private val api: PaymentApi) : RemoteDataSource() {

    suspend fun getPaymentMethods(): Result<List<PaymentMethod>> = call(api.getPaymentMethodsAsync())

    suspend fun payEvent(body: PayEventBody): Result<Any> = call(api.payEventAsync(body))


    suspend fun getQRData(url: String): Result<String> = withContext(IO) {
        if (NetworkMonitor.isConnected.not()) {
            return@withContext Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        var response: Response? = null
        var data = ""

        try {
            val request = Request.Builder()
                    .url(url)
                    .build()

            response = createClient().newCall(request).execute()

            if (response.isSuccessful) {
                data = response.body?.string() ?: ""
            } else {
                return@withContext Result.error(response.code, response.message)
            }

        } catch (exception: IOException) {
            return@withContext Result.error(ERR_NO_STATUS_CODE, exception.message)
        } finally {
            response?.close()
        }
        return@withContext Result.success(data)
    }

    suspend fun getQRCodeImage(url: String): Result<QRCodeImage> = call(api.getQRCodeImageAsync(url))

    private fun createClient() = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
}