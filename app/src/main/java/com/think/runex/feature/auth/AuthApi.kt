package com.think.runex.feature.auth

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig.Companion.API_VERSION
import com.think.runex.feature.auth.data.AccessToken
import com.think.runex.config.AppConfig
import com.think.runex.datasource.ResultAuth
import com.think.runex.feature.auth.data.request.AuthCode
import com.think.runex.feature.auth.data.request.AuthWithCodeBody
import com.think.runex.feature.auth.data.request.FirebaseTokenBody
import com.think.runex.feature.social.UserProvider
import com.think.runex.feature.social.UserProviderCreate
import com.think.runex.feature.user.data.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi {

    @GET("/api/${API_VERSION}/config")
    fun getAppConfigAsync(): Deferred<Result<AppConfig>>

    @POST
    fun authWithCodeAsync(@Url url: String,
                          @Body body: AuthWithCodeBody): Deferred<AccessToken>

    @POST
    fun createUserAsync(@Url url: String,
                          @Body body: UserProviderCreate): Deferred<ResultAuth<AuthCode>>

    @POST
    fun authWithOpenIDAsync(@Url url: String,
                          @Body body: UserProvider): Deferred<AccessToken>

    @GET("/api/${API_VERSION}/user")
    fun getUserInfoAsync(): Deferred<Result<UserInfo>>

    @POST("/api/${API_VERSION}/registerFirebase")
    fun sendFirebaseTokenToServerAsync(@Body body: FirebaseTokenBody): Deferred<Result<Any>>

    @POST("/api/${API_VERSION}/logout")
    fun logoutAsync(@Body body: FirebaseTokenBody): Deferred<Result<Any>>

    @GET("/api/${API_VERSION}/logout")
    fun logoutWithoutFirebaseTokenAsync(): Deferred<Result<Any>>

}