package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.config.AUTHORIZATION
import com.think.runex.feature.event.data.*
import com.think.runex.feature.user.UserInfoApi
import com.think.runex.feature.user.data.UserInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface EventApi : UserInfoApi {

    @GET("/api/${ApiConfig.API_VERSION}/event/all")
    fun getAllEventAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<EventItem>>>

    @GET("/api/${ApiConfig.API_VERSION}/event/detail/{eventCode}")
    fun getEventDetailsAsync(
            @Path("eventCode") eventCode: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<EventDetail>>

    @GET("/api/${ApiConfig.API_VERSION}/register/checkRegEventCode/{eventCode}")
    fun isRegisteredEventAsync(
            @Path("eventCode") eventCode: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<IsRegisteredEvent>>

    @GET("/api/${ApiConfig.API_VERSION}/register/myRegEvent")
    fun getMyEventsAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<Registered>>>

    @GET("/api/${ApiConfig.API_VERSION}/register/myRegEventActivate")
    fun getMyEventsAtActiveAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<Registered>>>

    @POST("/api/${ApiConfig.API_VERSION}/register/info")
    fun getRegisterDataAsync(
            @Body body: RegisteredRequestBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Registered>>

    @POST("/api/${ApiConfig.API_VERSION}/register/add")
    fun registerEventAsync(
            @Body body: JsonObject,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Registered>>

    @POST("/api/${ApiConfig.API_VERSION}/register/regUpdateUserInfo")
    fun updateRegisterInfoAsync(
            @Body body: JsonObject,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/register/team")
    fun addMemberToTeamAsync(
            @Body body: JsonObject,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>

}