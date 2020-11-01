package com.think.runex.feature.event

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.event.model.Event
import com.think.runex.feature.event.model.response.IsRegisteredEventResponse
import com.think.runex.util.KEY_AUTH
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface EventApi {

    @GET("/api/${ApiConfig.API_VERSION}/event/active")
    fun getAllEventAsync(@Header(KEY_AUTH) token: String = TokenManager.accessToken): Deferred<Result<List<Event>>>

    @GET("/api/${ApiConfig.API_VERSION}/register/checkUserRegisterEvent/{eventId}")
    fun isRegisteredEventAsync(
            @Path("eventId") eventId: String,
            @Header(KEY_AUTH) token: String = TokenManager.accessToken): Deferred<Result<IsRegisteredEventResponse>>
}