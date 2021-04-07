package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.event.data.*
import com.think.runex.feature.user.UserInfoApi
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface EventApi : UserInfoApi {

    @GET("/api/${ApiConfig.API_VERSION}/event/all")
    fun getAllEventAsync(): Deferred<Result<List<EventItem>>>

    @GET("/api/${ApiConfig.API_VERSION}/event/detail/{eventCode}")
    fun getEventDetailsAsync(@Path("eventCode") eventCode: String): Deferred<Result<EventDetail>>

    @GET("/api/${ApiConfig.API_VERSION}/register/checkRegEventCode/{eventCode}")
    fun isRegisteredEventAsync(@Path("eventCode") eventCode: String): Deferred<Result<IsRegisteredEvent>>

    @GET("/api/${ApiConfig.API_VERSION}/register/myRegEvent")
    fun getMyEventsAsync(): Deferred<Result<List<Registered>>>

    @GET("/api/${ApiConfig.API_VERSION}/register/myRegEventActivate")
    fun getMyEventsAtActiveAsync(): Deferred<Result<List<Registered>>>

    @POST("/api/${ApiConfig.API_VERSION}/register/info")
    fun getRegisterDataAsync(@Body body: RegisteredRequestBody): Deferred<Result<Registered>>

    @POST("/api/${ApiConfig.API_VERSION}/register/add")
    fun registerEventAsync(@Body body: JsonObject): Deferred<Result<Registered>>

    @POST("/api/${ApiConfig.API_VERSION}/register/regUpdateUserInfo")
    fun updateRegisterInfoAsync(@Body body: JsonObject): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/register/team")
    fun addMemberToTeamAsync(@Body body: JsonObject): Deferred<Result<Any>>

}