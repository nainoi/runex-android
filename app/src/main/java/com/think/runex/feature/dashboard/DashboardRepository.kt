package com.think.runex.feature.dashboard

import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.dashboard.data.DeleteActivityBody
import com.think.runex.feature.user.data.UserInfoRequestBody

class DashboardRepository(private val api: DashboardApi) : RemoteDataSource() {

    suspend fun getDashboardInfo(body: DashboardInfoRequestBody) = call(api.getDashboardInfoAsync(body))

    suspend fun getMyUserInfo() = call(api.getUserInfoAsync())

    suspend fun getUserInfoById(body: UserInfoRequestBody) = call(api.getUserInfoByIdAsync(body))

    suspend fun deleteActivity(body: DeleteActivityBody) = call(api.deleteActivityAsync(body))

}