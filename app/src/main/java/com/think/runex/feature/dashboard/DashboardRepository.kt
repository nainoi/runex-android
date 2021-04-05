package com.think.runex.feature.dashboard

import com.google.gson.JsonObject
import com.think.runex.util.extension.toJson
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class DashboardRepository(private val api: DashboardApi) : RemoteDataSource() {

    suspend fun getEventDashboard(eventCode: String,
                                  orderId: String,
                                  registerId: String,
                                  parentRegisterId: String): Result<DashboardInfo> {

        val jsonObject = JsonObject().apply {
            addProperty("event_code", eventCode)
            addProperty("order_id", orderId)
            addProperty("reg_id", registerId)
            addProperty("parent_reg_id", parentRegisterId)
        }

        return call(api.getDashboardAsync(jsonObject))
    }

    suspend fun getUserInfoById(userId: String): Result<UserInfo> {

        val jsonObject = JsonObject().apply {
            addProperty("uid", userId)
        }

        return call(api.getUserInfoByIdAsync(jsonObject))
    }
}