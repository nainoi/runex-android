package com.think.runex.feature.user

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.utility.Logger
import com.think.runex.common.getErrorMessage
import com.think.runex.config.ERR_NO_INTERNET_CONNECTION
import com.think.runex.datasource.Result
import com.think.runex.feature.auth.TokenManager

class UserRemoteDataSource {

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