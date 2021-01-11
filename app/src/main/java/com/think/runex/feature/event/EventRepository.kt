package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.model.EventItem
import com.think.runex.feature.event.model.EventRegistered
import com.think.runex.feature.event.model.response.EventDetailResponse

class EventRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getAllEvents(): Result<List<EventItem>> = call(api.getAllEventAsync())

    suspend fun getMyEvents(): Result<List<EventRegistered>> = call(api.getMyEventAsync())

    suspend fun getEventDetails(code: String): Result<EventDetailResponse> = call(api.getEventDetailsAsync(code))

    suspend fun isRegisteredEvent(code: String) = call(api.isRegisteredEventAsync(code))

    suspend fun registerEventWithKao(body: JsonObject) = call(api.registerEventWithKaoAsync(body))

}