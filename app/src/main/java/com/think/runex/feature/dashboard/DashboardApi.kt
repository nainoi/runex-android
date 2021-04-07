package com.think.runex.feature.dashboard

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.user.UserInfoApi
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DashboardApi : UserInfoApi {

    @POST("/api/${ApiConfig.API_VERSION}/activity/dashboard")
    fun getDashboardInfoAsync(
            @Body body: DashboardInfoRequestBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<DashboardInfo>>
}