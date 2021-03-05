package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.config.AUTHORIZATION
import com.think.runex.feature.user.data.UpdateProfileImageResult
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.workout.data.TotalDistance
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApi {

    @GET("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>

    @PUT("/api/${ApiConfig.API_VERSION}/user")
    fun updateUserInfoAsync(
            @Body userInfo: UserInfo,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/uploads")
    fun updateProfileImageAsync(
            @Body body: MultipartBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UpdateProfileImageResult>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts")
    fun getTotalDistancesAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<TotalDistance>>
}