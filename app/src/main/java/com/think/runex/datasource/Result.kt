package com.think.runex.datasource

import com.google.gson.annotations.SerializedName
import com.think.runex.util.KEY_DATA
import com.think.runex.util.KEY_ERROR
import com.think.runex.util.KEY_MESSAGE
import com.think.runex.util.KEY_STATUS_CODE
import java.net.HttpURLConnection

data class Result<T>(
        @SerializedName(KEY_STATUS_CODE) var statusCode: Int = 0,
        @SerializedName(KEY_ERROR) var error: String? = null,
        @SerializedName(KEY_MESSAGE) var message: String? = null,
        @SerializedName(KEY_DATA) var data: T? = null) {

    companion object {
        fun <T> success(data: T?, message: String?): Result<T> = Result(HttpURLConnection.HTTP_OK, null, message, data)
        fun <T> error(statusCode: Int, error: String?): Result<T> = Result(statusCode, error)
    }

    fun isSuccessful(): Boolean = statusCode == HttpURLConnection.HTTP_OK

    fun errorMessage() = error ?: message ?: ""
}