package com.think.runex.datasource

import com.google.gson.annotations.SerializedName
import com.think.runex.config.KEY_DATA
import com.think.runex.config.KEY_MESSAGE
import com.think.runex.config.KEY_STATUS_CODE
import java.net.HttpURLConnection

data class Result<T>(
        @SerializedName(KEY_STATUS_CODE) var statusCode: Int = 0,
        @SerializedName(KEY_MESSAGE) var message: String? = "",
        @SerializedName(KEY_DATA) var data: T? = null) {

    companion object {
        fun <T> success(data: T?): Result<T> = Result(HttpURLConnection.HTTP_OK, null, data)
        fun <T> error(statusCode: Int, message: String?): Result<T> = Result(statusCode, message, null)
    }

    fun isSuccessful(): Boolean = statusCode == HttpURLConnection.HTTP_OK
}