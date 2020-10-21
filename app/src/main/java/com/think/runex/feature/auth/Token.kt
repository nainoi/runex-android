package com.think.runex.feature.auth

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.toTimeMillis

data class Token(
        @SerializedName("token") var token: String = "",
        @SerializedName("expire") var expire: String = "",
        @SerializedName("tokenType") var tokenType: String = "Bearer") {

    private var serverDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun getExpiresIn(): Long = when (expire.isNotBlank()) {
        true -> expire.toTimeMillis(serverDateTimeFormat)
        false -> 0
    }

    fun convertToAccessToken() = AccessToken().apply {
        this.accessToken = token
        this.tokenType = tokenType
    }

    class Deserializer : ResponseDeserializable<Token> {
        override fun deserialize(content: String): Token = Gson().fromJson(content, Token::class.java)
    }
}