package com.think.runex.feature.event.team

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.*
import com.think.runex.feature.event.team.data.TeamImage
import com.think.runex.feature.user.data.UpdateProfileImageResult
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody
import okhttp3.MultipartBody

class TeamRepository(private val api: TeamApi) : RemoteDataSource() {

    suspend fun getRegisterData(body: RegisteredRequestBody): Result<Registered> = call(api.getRegisterDataAsync(body))

    suspend fun getUserInfoById(body: UserInfoRequestBody): Result<UserInfo> = call(api.getUserInfoByIdAsync(body))

    suspend fun addMEmberToTeam(body: JsonObject): Result<Any> = call(api.addMemberToTeamAsync(body))

    suspend fun getMyUserInfo() = call(api.getUserInfoAsync())

    suspend fun updateTeamInfo(body: JsonObject): Result<Any> = call(api.updateTeamInfoAsync(body))

    suspend fun getTeamImage(body: TeamImage): Result<TeamImage> = call(api.getTeamImageAsync(body))

    suspend fun uploadTeamImage(body: MultipartBody): Result<TeamImage> = call(api.uploadTeamImageAsync(body))

}