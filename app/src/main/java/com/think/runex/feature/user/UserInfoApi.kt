package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserInfoApi {

    @GET("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoAsync(): Deferred<Result<UserInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoByIdAsync(@Body body: UserInfoRequestBody): Deferred<Result<UserInfo>>
}