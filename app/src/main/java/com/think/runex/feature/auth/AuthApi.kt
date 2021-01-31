package com.think.runex.feature.auth

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.request.AuthenWithCodeRequest
import com.think.runex.feature.auth.request.FirebaseTokenRequest
import com.think.runex.feature.user.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi {

    @GET("/api/${ApiConfig.API_VERSION}/config")
    fun getApiConfigAsync(): Deferred<Result<ApiConfigResponse>>

    @POST
    fun authenWithCodeAsync(
            @Url url: String,
            @Body body: AuthenWithCodeRequest): Deferred<AccessToken>

    @GET("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/registerFirebase")
    fun sendFirebaseTokenToServerAsync(
            @Body body: FirebaseTokenRequest,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/logout")
    fun logoutAsync(
            @Body body: FirebaseTokenRequest,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @GET("/api/${ApiConfig.API_VERSION}/logout")
    fun logoutWithoutFirebaseTokenAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

}