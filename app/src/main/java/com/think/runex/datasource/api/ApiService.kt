package com.think.runex.datasource.api

import androidx.annotation.NonNull
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.think.runex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    fun <T> provideService(@NonNull service: Class<T>,
                           baseUrl: String = ApiConfig.BASE_URL,
                           httpLoggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): T {
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(createOkHttpClient(httpLoggingLevel).build())
                .build()
                .create(service)
    }


    /**
     * Create OKHttp client with Logging Interceptor.
     * On production not have add interceptor
     *
     *
     * @return OkHttpClient
     */
    fun createOkHttpClient(httpLoggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = httpLoggingLevel
                        })
                    }
                }
    }
}