package com.think.runex.feature.auth

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class AccessToken(
        @SerializedName("runex_access_token") var accessToken: String = "",
        @SerializedName("runex_refresh_token") var refreshToken: String = "",
        @SerializedName("expires_in") var expiresIn: Long = 0,
        @SerializedName("tokenType") var tokenType: String = "Bearer") {


    class Deserializer : ResponseDeserializable<AccessToken> {
        override fun deserialize(content: String): AccessToken = Gson().fromJson(content, AccessToken::class.java)
    }
}