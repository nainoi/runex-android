package com.think.runex.feature.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.think.runex.common.toJson
import com.think.runex.common.toObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.auth.request.AuthenWithCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.util.KEY_ACCESS_TOKEN
import com.think.runex.util.KEY_API

class AuthRepository(private val api: AuthApi,
                     private val preferences: SharedPreferences) : RemoteDataSource() {

    fun getLocalAccessToken(): AccessToken {
        return preferences.getString(KEY_ACCESS_TOKEN, "")
                .toObject(AccessToken::class.java) ?: AccessToken()
    }

    fun setLocalAccessToken(accessToken: AccessToken) {
        preferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken.toJson())
        }
    }

    fun setLocalApiConfig(config: ApiConfigResponse) {
        preferences.edit {
            putString(KEY_API, config.toJson())
        }
    }

    fun getLocalApiConfig(): ApiConfigResponse {
        return preferences.getString(KEY_API, "")
                .toObject(ApiConfigResponse::class.java) ?: ApiConfigResponse()
    }


    suspend fun loginWithCode(body: AuthenWithCodeRequest): Result<AccessToken> {
        return calls(api.authenWithCodeAsync(ApiConfig.AUTH_URL, body))
    }

    suspend fun getUserInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun getApiConfig(): Result<ApiConfigResponse> = call(api.getApiConfigAsync())
}