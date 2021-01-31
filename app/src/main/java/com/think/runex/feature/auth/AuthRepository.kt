package com.think.runex.feature.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.think.runex.common.toJson
import com.think.runex.common.toObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.auth.request.AuthenWithCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.config.KEY_ACCESS_TOKEN
import com.think.runex.config.KEY_API
import com.think.runex.config.KEY_FIREBASE_TOKEN
import com.think.runex.feature.auth.request.FirebaseTokenRequest

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

    fun removeLocalAccessToken() {
        preferences.edit {
            remove(KEY_ACCESS_TOKEN)
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

    fun getFirebaseToken(): String? {
        return preferences.getString(KEY_FIREBASE_TOKEN, null)
    }

    fun setFirebaseToken(firebaseToken: String) {
        preferences.edit {
            putString(KEY_FIREBASE_TOKEN, firebaseToken)
        }
    }
//
//    fun removeFireBaseToken(context: Context) = createPreferenceEncrypted(context).edit {
//        remove(KEY_FIREBASE_TOKEN)
//    }

    fun isUpdatedFirebaseToken(): Boolean {
        return preferences.getBoolean("is_update_firebase_token", false)
    }

    fun setUpdatedFirebaseToken(isUpdated: Boolean) {
        preferences.edit {
            putBoolean("is_update_firebase_token", isUpdated)
        }
    }

    suspend fun sendFirebaseTokenToServer(firebaseToken: String): Result<Any> {
        return call(api.sendFirebaseTokenToServerAsync(FirebaseTokenRequest(firebaseToken)))
    }

    suspend fun loginWithCode(body: AuthenWithCodeRequest): Result<AccessToken> {
        return calls(api.authenWithCodeAsync(ApiConfig.AUTH_URL, body))
    }

    suspend fun getUserInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun getApiConfig(): Result<ApiConfigResponse> = call(api.getApiConfigAsync())

    suspend fun logout(): Result<Any> {
        val firebaseToken = getFirebaseToken()
        return when (firebaseToken?.isNotBlank() == true) {
            true -> call(api.logoutAsync(FirebaseTokenRequest(firebaseToken)))
            false -> call(api.logoutWithoutFirebaseTokenAsync())
        }
    }
}