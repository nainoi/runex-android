package com.think.runex.feature.auth

import android.content.SharedPreferences
import com.think.runex.datasource.local.getAccessToken
import com.think.runex.datasource.local.setAccessToken

class AuthLocalDataSource(private val preferences: SharedPreferences) {

    fun getAccessToken() = preferences.getAccessToken()

    fun setAccessToken(accessToken: Token) {
        preferences.setAccessToken(accessToken)
    }
}