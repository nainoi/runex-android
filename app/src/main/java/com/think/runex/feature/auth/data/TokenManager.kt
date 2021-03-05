package com.think.runex.feature.auth.data

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