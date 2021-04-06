package com.think.runex.feature.user


import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserInfoApi {

    @GET("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoByIdAsync(
            @Body body: UserInfoRequestBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>
}