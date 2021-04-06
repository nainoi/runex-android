package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.user.data.UpdateProfileImageResult
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.workout.data.TotalDistance
import okhttp3.MultipartBody

class UserRepository(private val api: UserApi) : RemoteDataSource() {

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun updateUserInfo(userInfo: UserInfo): Result<UserInfo> = call(api.updateUserInfoAsync(userInfo))

    suspend fun uploadProfileImage(body: MultipartBody): Result<UpdateProfileImageResult> = call(api.updateProfileImageAsync(body))

    suspend fun getTotalDistances(): Result<TotalDistance> = call(api.getTotalDistancesAsync())

}