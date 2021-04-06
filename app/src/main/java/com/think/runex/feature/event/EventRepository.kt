package com.think.runex.feature.event

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.*

class EventRepository(private val api: EventApi) : RemoteDataSource() {

    suspend fun getAllEvents(): Result<List<EventItem>> = call(api.getAllEventAsync())

    suspend fun getEventDetails(eventCode: String): Result<EventDetail> = call(api.getEventDetailsAsync(eventCode))

    suspend fun isRegisteredEvent(eventCode: String): Result<IsRegisteredEvent> = call(api.isRegisteredEventAsync(eventCode))

    suspend fun getMyEvents(): Result<List<Registered>> = call(api.getMyEventsAsync())

    suspend fun getMyEventsAtActive(): Result<List<Registered>> = call(api.getMyEventsAtActiveAsync())

    suspend fun registerEvent(body: JsonObject): Result<Registered> = call(api.registerEventAsync(body))

    suspend fun updateRegisterInfo(body: JsonObject): Result<Any> = call(api.updateRegisterInfoAsync(body))

}