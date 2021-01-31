package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.model.TotalDistanceResponse


class UserRepository(private val api: UserApi) : RemoteDataSource() {

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun getTotalDistances(): Result<TotalDistanceResponse> = call(api.getTotalDistancesAsync())
}