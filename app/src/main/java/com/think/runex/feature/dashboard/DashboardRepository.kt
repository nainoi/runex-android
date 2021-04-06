package com.think.runex.feature.dashboard

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody

class DashboardRepository(private val api: DashboardApi) : RemoteDataSource() {

    suspend fun getDashboardInfo(body: DashboardInfoRequestBody): Result<DashboardInfo> = call(api.getDashboardInfoAsync(body))

    suspend fun getMyUserInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun getUserInfoById(body: UserInfoRequestBody): Result<UserInfo> = call(api.getUserInfoByIdAsync(body))

}