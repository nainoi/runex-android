package com.think.runex.feature.event.team

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.*
import com.think.runex.feature.user.data.UserInfo

class TeamRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getRegisterData(eventCode: String, registerId: String, parentRegisterId: String): Result<Registered> {

        val body = JsonObject().apply {
            addProperty("event_code", eventCode)
            addProperty("reg_id", registerId)
            addProperty("parent_reg_id", parentRegisterId)
        }

        return call(api.getRegisterDataAsync(body))
    }

    suspend fun getUserInfoById(userId: String): Result<UserInfo> {

        val body = JsonObject().apply { addProperty("uid", userId) }

        return call(api.getUserInfoByIdAsync(body))
    }

    suspend fun addMEmberToTeam(body: JsonObject): Result<Any> = call(api.addMemberToTeamAsync(body))

}