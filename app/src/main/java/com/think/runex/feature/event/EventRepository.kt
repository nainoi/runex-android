package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.model.Event
import com.think.runex.feature.event.model.registered.RegisteredEvent

class EventRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getAllEvents(): Result<List<Event>> = call(api.getAllEventAsync())

    suspend fun getMyEvents(): Result<List<RegisteredEvent>> = call(api.getMyEventAsync())

    suspend fun isRegisteredEvent(eventId: String) = call(api.isRegisteredEventAsync(eventId))

    suspend fun registerEventWithKao(body: JsonObject) = call(api.registerEventWithKaoAsync(body))

}