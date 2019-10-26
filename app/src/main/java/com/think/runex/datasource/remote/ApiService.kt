package com.think.runex.datasource.remote

import androidx.annotation.NonNull
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.think.runex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {

    fun <T> provideService(@NonNull service: Class<T>, isLogging: Boolean = true): T = Retrofit.Builder()
            .baseUrl(ApiUrl.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createClient(isLogging))
            .build()
            .create(service)

    /**
     * Create OKHttp client with Logging Interceptor.
     * On production not have add interceptor
     *
     * @return OkHttpClient
     */
    private fun createClient(isLogging: Boolean = true): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (isLogging) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.HEADERS
                    })
                }
            }
            .build()

}