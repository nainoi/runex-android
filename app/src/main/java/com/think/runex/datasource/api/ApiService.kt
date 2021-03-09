package com.think.runex.datasource.api

import android.content.Context
import androidx.annotation.NonNull
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.think.runex.BuildConfig
import com.think.runex.feature.auth.RefreshTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    fun <T> provideService(context: Context,
                           @NonNull service: Class<T>,
                           baseUrl: String = ApiConfig.BASE_URL,
                           httpLoggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): T {
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(createOkHttpClient(context, httpLoggingLevel).build())
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
    fun createOkHttpClient(context: Context,
                           httpLoggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = httpLoggingLevel
                        })
                        addInterceptor(RefreshTokenInterceptor(context))
                    }
                }
    }
}