package com.think.runex.feature.event.team

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.toJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class TeamRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getEventRegisteredInfo(body: JsonObject): Result<EventRegistered> = call(api.getEventRegisteredInfoAsync(body))

    suspend fun getUserInfoById(userId: String): Result<UserInfo> {

        val jsonObject = JsonObject().apply { addProperty("uid", userId) }
        val body = jsonObject.toJson().toRequestBody("application/json; charset=utf-8".toMediaType())

        return call(api.getUserInfoByIdAsync(body))
    }
}