package com.think.runex.feature.event.team

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.*
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody

class TeamRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getRegisterData(body: RegisteredRequestBody): Result<Registered> = call(api.getRegisterDataAsync(body))

    suspend fun getUserInfoById(body: UserInfoRequestBody): Result<UserInfo> = call(api.getUserInfoByIdAsync(body))

    suspend fun addMEmberToTeam(body: JsonObject): Result<Any> = call(api.addMemberToTeamAsync(body))

}