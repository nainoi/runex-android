package com.think.runex.feature.auth

import com.google.gson.annotations.SerializedName
import com.think.runex.common.toTimeStamp

data class AccessToken(
        @SerializedName("token") var token: String = "",
        @SerializedName("expire") var expire: String = "",
        @SerializedName("tokenType") var tokenType: String = "Bearer") {

    private var serverDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun getExpiresIn(): Long = when (expire.isNotBlank()) {
        true -> expire.toTimeStamp(serverDateTimeFormat)
        false -> 0
    }
}