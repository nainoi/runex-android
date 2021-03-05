package com.think.runex.feature.auth.data.request

import com.google.gson.annotations.SerializedName

data class AuthWithCodeBody(
        @SerializedName("code") var code: String,
        @SerializedName("grant_type") var grantType: String = "authorization_code")