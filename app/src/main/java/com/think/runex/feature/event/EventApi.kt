package com.think.runex.feature.event

import com.think.runex.datasource.Result
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.event.model.Event
import com.think.runex.util.KEY_AUTH
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header

interface EventApi {

    @GET("/api/v1/event/active")
    fun getAllEventAsync(@Header(KEY_AUTH) token: String = TokenManager.accessToken): Deferred<Result<List<Event>>>
}