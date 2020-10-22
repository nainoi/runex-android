package com.think.runex.feature.auth

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.request.LoginCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.util.KEY_AUTH
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("https://auth.runex.co/v1/oauth2/token")
    fun authorizationWithCodeAsync(@Body body: LoginCodeRequest): Deferred<AccessToken>

    @GET("user")
    fun getUserInfoAsync(@Header(KEY_AUTH) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>
}