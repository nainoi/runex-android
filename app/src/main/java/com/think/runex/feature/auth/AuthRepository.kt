package com.think.runex.feature.auth

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.fuel.gson.jsonBody
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.utility.Logger
import com.think.runex.common.getErrorMessage
import com.think.runex.config.ERR_NO_INTERNET_CONNECTION
import com.think.runex.datasource.Result
import com.think.runex.feature.user.Profile
import com.think.runex.feature.user.UserUrl


class AuthRepository(private val localDataSource: AuthLocalDataSource) {

    fun getAccessToken() = localDataSource.getAccessToken()

    fun setAccessToken(accessToken: Token) = localDataSource.setAccessToken(accessToken)


    suspend fun register(body: RegisterRequest): Result<Token> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        return Fuel.post(AuthUrl.REGISTER_PATH)
                .jsonBody(body)
                .response { request, response, _ ->
                    if (Logger.isLogging) {
                        println(request)
                        println(response)
                    }
                }
                .awaitObjectResult(Token.Deserializer())
                .fold(success = { token ->
                    Result.success(token)
                }, failure = { error ->
                    Result.error(error.response.statusCode, error.getErrorMessage())
                })
    }

    suspend fun login(body: LoginRequest): Result<Token> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        return Fuel.post(AuthUrl.LOGIN_PATH)
                .jsonBody(body)
                .response { request, response, _ ->
                    if (Logger.isLogging) {
                        println(request)
                        println(response)
                    }
                }
                .awaitObjectResult(Token.Deserializer())
                .fold(success = { token ->
                    Result.success(token)
                }, failure = { error ->
                    Result.error(error.response.statusCode, error.getErrorMessage())
                })
    }

    suspend fun getProfile(): Result<Profile> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        return Fuel.get(UserUrl.PROFILE_PATH)
                .authentication()
                .bearer(TokenManager.bearerToken())
                .response { request, response, _ ->
                    if (Logger.isLogging) {
                        println(request)
                        println(response)
                    }
                }
                .awaitObjectResult(gsonDeserializer<Result<Profile>>())
                .fold(success = {
                    it
                }, failure = { error ->
                    Result.error(error.response.statusCode, error.getErrorMessage())
                })
    }
}