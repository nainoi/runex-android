package com.think.runex.feature.dashboard

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.event.data.request.EventDashboardBody
import com.think.runex.feature.user.data.UserInfo
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DashboardApi {

    @POST("/api/${ApiConfig.API_VERSION}/activity/dashboard")
    fun getDashboardAsync(
            @Body body: EventDashboardBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<DashboardInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoByIdAsync(@Body body: RequestBody,
                             @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>
}