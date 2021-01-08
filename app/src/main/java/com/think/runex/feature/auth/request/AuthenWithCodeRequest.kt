package com.think.runex.feature.auth.request

import com.google.gson.annotations.SerializedName

data class AuthenWithCodeRequest(
        @SerializedName("code") var code: String,
        @SerializedName("grant_type") var grantType: String = "authorization_code")