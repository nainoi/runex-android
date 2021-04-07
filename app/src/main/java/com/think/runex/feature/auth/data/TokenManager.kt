package com.think.runex.feature.auth.data

class TokenManager {
    companion object {

        private var tokenType: String = ""

        private var accessToken: String = ""

        var refreshToken: String = ""
            private set

        //var expiresIn: Long = 0
        //    private set

        fun isAlive(): Boolean = accessToken.isNotBlank()

        fun accessToken(withTokenType: Boolean = true): String {
            if (withTokenType && accessToken.isNotBlank()) {
                return "$tokenType $accessToken"
            }
            return accessToken
        }

        fun tokenType() = tokenType

        fun updateToken(accessToken: AccessToken) {
            tokenType = accessToken.tokenType
            Companion.accessToken = accessToken.accessToken
            refreshToken = accessToken.refreshToken
            //this.expiresIn = accessToken.expiresIn
        }

        //private var userId: String = ""

        //fun userId() = userId

        //fun updateUserId(userId: String) {
        //    this.userId = userId
        //}

        fun clearToken() {
            tokenType = ""
            accessToken = ""
            refreshToken = ""
            //expiresIn = 0
        }
    }
}