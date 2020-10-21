package com.think.runex.feature.auth

import com.think.runex.datasource.api.ApiConfig

class AuthUrl {
    companion object {
        const val SCHEME = "runex"
        const val WEB_LOGIN_URL = "${ApiConfig.AUTH_URL}/login?device=android"
    }
}