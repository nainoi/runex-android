package com.think.runex.datasource.remote

import com.think.runex.BuildConfig

class ApiUrl {
    companion object {
        const val KEY_AUTH = "Authorization"
        const val KEY_DATA = "data"
        const val KEY_MESSAGE = "msg"
        const val KEY_STATUS_CODE = "code"
        const val KEY_VERSION = "version"

        private const val BASE_URL: String = "http://farmme.in.th:3006"
        private const val API_VERSION: String = "v1"

        fun getApiVersion() = API_VERSION

        fun getBaseUrl() = when (BuildConfig.DEBUG) {
            true -> BASE_URL
            false -> BASE_URL
        }

        fun getUpLoadUrl() = when (BuildConfig.DEBUG) {
            true -> BASE_URL
            false -> BASE_URL
        }
    }
}