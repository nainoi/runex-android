package com.think.runex.feature.event

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.model.Event

class EventRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getAllEvent(): Result<List<Event>> = call(api.getAllEventAsync())

    suspend fun isRegisteredEvent(eventId: String) = call(api.isRegisteredEventAsync(eventId))
}