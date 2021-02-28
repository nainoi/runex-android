package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.event.model.response.IsRegisteredEventResponse
import com.think.runex.config.AUTHORIZATION
import com.think.runex.feature.event.model.EventDetail
import com.think.runex.feature.event.model.EventItem
import com.think.runex.feature.event.model.EventRegistered
import com.think.runex.feature.event.model.response.EventDetailResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface EventApi {

    @GET("/api/${ApiConfig.API_VERSION}/event/all")
    fun getAllEventAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<EventItem>>>

    @GET("/api/${ApiConfig.API_VERSION}/event/myEvent")
    fun getMyEventAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<EventRegistered>>>

    @GET("/api/${ApiConfig.API_VERSION}/event/detail/{code}")
    fun getEventDetailsAsync(
            @Path("code") code: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<EventDetail>>

    @GET("/api/${ApiConfig.API_VERSION}/register/checkUserRegisterEvent/{code}")
    fun isRegisteredEventAsync(
            @Path("code") code: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<IsRegisteredEventResponse>>

    @POST("/api/${ApiConfig.API_VERSION}/register/add")
    fun registerEventWithKaoAsync(
            @Body body: JsonObject,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<Any>>


}