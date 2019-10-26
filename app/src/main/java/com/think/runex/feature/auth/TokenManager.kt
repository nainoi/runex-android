package com.think.runex.feature.auth

class TokenManager {
    companion object {
        private var token: String = ""
        private var tokenType: String = ""
        private var expiresIn: Long = 0

        fun isAlive(): Boolean =  (System.currentTimeMillis() / 1000 < expiresIn) && token != "" && tokenType != ""

        fun accessToken(): String = "$tokenType $token"

        fun updateToken(token: String, tokenType: String, expiresIn: Long) {
            this.token = token
            this.tokenType = tokenType
            this.expiresIn = expiresIn
        }

        fun clearToken() {
            this.token = ""
            this.tokenType = ""
            this.expiresIn = 0
        }
    }
}