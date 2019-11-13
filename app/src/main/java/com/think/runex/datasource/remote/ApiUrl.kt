package com.think.runex.datasource.remote

import com.think.runex.BuildConfig

class ApiUrl {
    companion object {
        private const val BASE_URL: String = "https://runex.co:3006"
        private const val BASE_URL_DEBUG: String = "http://farmme.in.th:3006"

        const val API_VERSION: String = "v1"

        fun getApiVersion() = API_VERSION

        fun getBaseUrl(): String = if (BuildConfig.DEBUG) BASE_URL/*BASE_URL_DEBUG*/ else BASE_URL
    }
}