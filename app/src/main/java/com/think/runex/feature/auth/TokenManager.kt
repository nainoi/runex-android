package com.think.runex.feature.auth

class TokenManager {
    companion object {
        private var tokenType: String = ""

        var accessToken: String = ""
            private set
            get() {
                if (field.isBlank()) return field
                return "$tokenType $field"
            }

        var refreshToken: String = ""
            private set

        var expiresIn: Long = 0
            private set

        fun isAlive(): Boolean = accessToken.isNotBlank()

        fun updateToken(accessToken: AccessToken) {
            this.tokenType = accessToken.tokenType
            this.accessToken = accessToken.accessToken
            this.refreshToken = accessToken.refreshToken
            this.expiresIn = accessToken.expiresIn
        }

        fun clearToken() {
            tokenType = ""
            accessToken = ""
            refreshToken = ""
            expiresIn = 0
        }
    }
}