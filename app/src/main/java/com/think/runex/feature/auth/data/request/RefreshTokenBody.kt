package com.think.runex.feature.auth.data.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenBody(
        @SerializedName("access_token") var accessToken: String,
        @SerializedName("refresh_token") var refreshToken: String)