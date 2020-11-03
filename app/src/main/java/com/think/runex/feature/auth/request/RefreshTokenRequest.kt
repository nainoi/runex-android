package com.think.runex.feature.auth.request

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.auth.AccessToken

data class RefreshTokenRequest(
        @SerializedName("access_token") var accessToken: String,
        @SerializedName("refresh_token") var refreshToken: String) {

    fun toAccessToken() = AccessToken(accessToken, refreshToken)
}