package com.think.runex.feature.auth

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig.Companion.API_VERSION
import com.think.runex.feature.auth.data.AccessToken
import com.think.runex.config.AppConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.auth.data.request.AuthWithCodeBody
import com.think.runex.feature.auth.data.request.FirebaseTokenBody
import com.think.runex.feature.user.data.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi {

    @GET("/api/${API_VERSION}/config")
    fun getAppConfigAsync(): Deferred<Result<AppConfig>>

    @POST
    fun authWithCodeAsync(
            @Url url: String,
            @Body body: AuthWithCodeBody): Deferred<AccessToken>

    @GET("/api/${API_VERSION}/user")
    fun getUserInfoAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>

    @POST("/api/${API_VERSION}/registerFirebase")
    fun sendFirebaseTokenToServerAsync(
            @Body body: FirebaseTokenBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @POST("/api/${API_VERSION}/logout")
    fun logoutAsync(
            @Body body: FirebaseTokenBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @GET("/api/${API_VERSION}/logout")
    fun logoutWithoutFirebaseTokenAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

}