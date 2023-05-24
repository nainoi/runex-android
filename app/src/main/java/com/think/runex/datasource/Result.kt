package com.think.runex.datasource

import com.google.gson.annotations.SerializedName
import com.think.runex.config.KEY_DATA
import com.think.runex.config.KEY_MESSAGE
import com.think.runex.config.KEY_CODE
import java.net.HttpURLConnection

data class Result<T>(
        @SerializedName(KEY_CODE) var code: Int = 0,
        @SerializedName(KEY_MESSAGE) var message: String? = null,
        @SerializedName(KEY_DATA) var data: T? = null) {

    companion object {
        fun <T> success(data: T?, message: String? = null): Result<T> = Result(HttpURLConnection.HTTP_OK, message, data)
        fun <T> error(code: Int, message: String?): Result<T> = Result(code, message)
    }

    fun isSuccessful(): Boolean = code in 200..299
}

data class ResultAuth<T>(
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("data") var data: T? = null) {

    companion object {
        fun <T> success(data: T?, success: Boolean? = false): ResultAuth<T> = ResultAuth(true, data)
        fun <T> error(success: Boolean, message: String?): ResultAuth<T> = ResultAuth(false, null)
    }

    fun isSuccessful(): Boolean = success
}