package com.think.runex.feature.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.think.runex.config.*
import com.think.runex.util.extension.toJson
import com.think.runex.util.extension.toObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.auth.data.request.AuthWithCodeBody
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.AccessToken
import com.think.runex.datasource.ResultAuth
import com.think.runex.feature.auth.data.request.AuthCode
import com.think.runex.feature.auth.data.request.FirebaseTokenBody
import com.think.runex.feature.social.UserProvider
import com.think.runex.feature.social.UserProviderCreate

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

    fun setLocalAppConfig(config: AppConfig) {
        preferences.edit {
            putString(KEY_API, config.toJson())
        }
    }

    fun getLocalAppConfig(): AppConfig {
        return preferences.getString(KEY_API, "")
                .toObject(AppConfig::class.java) ?: AppConfig()
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

    fun getLocalCodeAccess(): String? {
        return preferences.getString(KEY_CODE_ACCESS, "")
    }

    fun setLocalCodeAccess(code: String) {
        preferences.edit {
            putString(KEY_CODE_ACCESS, "")
        }
    }

    suspend fun sendFirebaseTokenToServer(firebaseToken: String): Result<Any> {
        return call(api.sendFirebaseTokenToServerAsync(FirebaseTokenBody(firebaseToken)))
    }

    suspend fun loginWithCode(body: AuthWithCodeBody): Result<AccessToken> {
        return calls(api.authWithCodeAsync(ApiConfig.AUTH_URL, body))
    }

    suspend fun loginWithOpenID(body: UserProvider): Result<AccessToken> {
        return calls(api.authWithOpenIDAsync(ApiConfig.AUTH_URL, body))
    }

    suspend fun createUserProvider(body: UserProviderCreate): Result<ResultAuth<AuthCode>> {
        return calls(api.createUserAsync(ApiConfig.CREATE_USER_URL, body))
    }

    suspend fun getUserInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun getAppConfig(): Result<AppConfig> = call(api.getAppConfigAsync())

    suspend fun logout(): Result<Any> {
        val firebaseToken = getFirebaseToken()
        return when (firebaseToken?.isNotBlank() == true) {
            true -> call(api.logoutAsync(FirebaseTokenBody(firebaseToken)))
            false -> call(api.logoutWithoutFirebaseTokenAsync())
        }
    }
}