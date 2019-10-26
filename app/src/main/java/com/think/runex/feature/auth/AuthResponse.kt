package com.think.runex.feature.auth

import com.google.gson.annotations.SerializedName
import com.think.runex.datasource.remote.ApiUrl.Companion.KEY_MESSAGE
import com.think.runex.datasource.remote.ApiUrl.Companion.KEY_STATUS_CODE

data class AuthResponse(
        @SerializedName(KEY_STATUS_CODE) var statusCode: Int = 0,
        @SerializedName(KEY_MESSAGE) var message: String = "",
        @SerializedName("expire") var expire: String = "",
        @SerializedName("token") var token: String = "")