package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.EventItem
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.event.data.EventRegisteredData

class EventRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getAllEvents(): Result<List<EventItem>> = call(api.getAllEventAsync())

    suspend fun getMyEvents(): Result<List<EventRegistered>> = call(api.getMyEventAsync())

    suspend fun getEventDetails(code: String): Result<EventDetail> = call(api.getEventDetailsAsync(code))

    suspend fun isRegisteredEvent(code: String) = call(api.isRegisteredEventAsync(code))

    suspend fun registerEven(body: JsonObject) = call(api.registerEventAsync(body))

    suspend fun registerEventWithKao(body: JsonObject) = call(api.registerEventWithKaoAsync(body))

}