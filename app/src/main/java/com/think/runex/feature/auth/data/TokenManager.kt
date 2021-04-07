package com.think.runex.feature.auth.data

class TokenManager {
    companion object {
        private var tokenType: String = ""

        private var accessToken: String = ""

        fun accessToken(withTokenType: Boolean = true): String {
            if (withTokenType && accessToken.isNotBlank()) {
                return "$tokenType $accessToken"
            }
            return accessToken
        }

        var refreshToken: String = ""
            private set

        //var expiresIn: Long = 0
        //    private set

        fun isAlive(): Boolean = accessToken.isNotBlank()

        fun updateToken(accessToken: AccessToken) {
            tokenType = accessToken.tokenType
            Companion.accessToken = accessToken.accessToken
            refreshToken = accessToken.refreshToken
            //this.expiresIn = accessToken.expiresIn
        }

        fun clearToken() {
            tokenType = ""
            accessToken = ""
            refreshToken = ""
            //expiresIn = 0
        }
    }
}