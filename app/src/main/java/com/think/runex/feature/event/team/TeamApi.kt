package com.think.runex.feature.event.team

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.team.data.TeamImage
import com.think.runex.feature.user.data.UpdateProfileImageResult
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST

interface TeamApi : EventApi {

    @POST("/api/${ApiConfig.API_VERSION}/register/editUserInfo")
    fun updateTeamInfoAsync(@Body body: JsonObject): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/getTeamIcon")
    fun getTeamImageAsync(@Body body: TeamImage): Deferred<Result<TeamImage>>

    @POST("/api/${ApiConfig.API_VERSION}/teamIcon")
    fun uploadTeamImageAsync(@Body body: MultipartBody): Deferred<Result<TeamImage>>
}