package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.user.data.UpdateProfileImageResult
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.workout.data.TotalDistance
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApi : UserInfoApi {

    @PUT("/api/${ApiConfig.API_VERSION}/user")
    fun updateUserInfoAsync(@Body userInfo: UserInfo): Deferred<Result<UserInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/uploads")
    fun updateProfileImageAsync(@Body body: MultipartBody): Deferred<Result<UpdateProfileImageResult>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts")
    fun getTotalDistancesAsync(): Deferred<Result<TotalDistance>>

}