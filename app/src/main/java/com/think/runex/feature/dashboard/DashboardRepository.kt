package com.think.runex.feature.dashboard

import com.google.gson.JsonObject
import com.think.runex.util.extension.toJson
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.request.EventDashboardBody
import com.think.runex.feature.user.data.UserInfo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class DashboardRepository(private val api: DashboardApi) : RemoteDataSource() {

    suspend fun getEventDashboard(body: EventDashboardBody) = call(api.getDashboardAsync(body))

    suspend fun getUserInfoById(userId: String): Result<UserInfo> {

        val jsonObject = JsonObject().apply { addProperty("uid", userId) }
        val body = jsonObject.toJson().toRequestBody("application/json; charset=utf-8".toMediaType())

        return call(api.getUserInfoByIdAsync(body))
    }
}