package com.think.runex.feature.auth.request

import com.google.gson.annotations.SerializedName
import com.think.runex.config.ANDROID

data class LoginEmailRequest(
        @SerializedName("email") var email: String,
        @SerializedName("password") var password: String,
        @SerializedName("PF") var platform: String = ANDROID)