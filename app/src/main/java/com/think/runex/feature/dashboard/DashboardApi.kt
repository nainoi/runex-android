package com.think.runex.feature.dashboard

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.dashboard.data.DeleteActivityBody
import com.think.runex.feature.dashboard.data.UserActivityDashboard
import com.think.runex.feature.user.UserInfoApi
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface DashboardApi : UserInfoApi {

    @POST("/api/${ApiConfig.API_VERSION}/activity/dashboard")
    fun getDashboardInfoAsync(@Body body: DashboardInfoRequestBody): Deferred<Result<DashboardInfo>>

    @HTTP(method = "DELETE", path = "/api/${ApiConfig.API_VERSION}/activity/remove", hasBody = true)
    fun deleteActivityAsync(@Body body: DeleteActivityBody): Deferred<Result<UserActivityDashboard>>
}