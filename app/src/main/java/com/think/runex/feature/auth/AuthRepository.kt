package com.think.runex.feature.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.util.Logger
import com.think.runex.common.getErrorMessage
import com.think.runex.common.toJson
import com.think.runex.common.toObject
import com.think.runex.util.ERR_NO_INTERNET_CONNECTION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.auth.request.LoginCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.feature.user.UserUrl
import com.think.runex.util.KEY_ACCESS_TOKEN


class AuthRepository(
        private val api: AuthApi,
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
//
//    suspend fun loginCode(body: LoginCodeRequest): Result<AccessToken> {
//        if (NetworkMonitor.isConnected.not()) {
//            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
//        }
//
//        val params = listOf("code" to body.code, "grant_type" to body.grantType)
//        return Fuel.post(AuthUrl.TOKEN_PATH, params)
//                //.jsonBody(body)
//                .response { request, response, _ ->
//                    if (Logger.isLogging) {
//                        println(request)
//                        println(response)
//                    }
//                }
//                .awaitObjectResult(AccessToken.Deserializer())
//                .fold(success = { accessToken ->
//                    Result.success(accessToken, null)
//                }, failure = { error ->
//                    Result.error(error.response.statusCode, error.getErrorMessage())
//                })
//    }

    suspend fun loginWithCode(body: LoginCodeRequest): Result<AccessToken> = calls(api.authorizationWithCodeAsync(body))

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

//    suspend fun getProfile(): Result<UserInfo> {
//        if (NetworkMonitor.isConnected.not()) {
//            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
//        }
//
//        return Fuel.get(UserUrl.PROFILE_PATH)
//                .authentication()
//                .bearer(TokenManager.accessToken)
//                .response { request, response, _ ->
//                    if (Logger.isLogging) {
//                        println(request)
//                        println(response)
//                    }
//                }
//                .awaitObjectResult(gsonDeserializer<Result<UserInfo>>())
//                .fold(success = {
//                    it
//                }, failure = { error ->
//                    Result.error(error.response.statusCode, error.getErrorMessage())
//                })
//    }
}