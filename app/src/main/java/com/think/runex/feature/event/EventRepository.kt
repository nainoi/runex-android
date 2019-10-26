package com.think.runex.feature.event

import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.RemoteDataSource
import com.think.runex.feature.product.Product

class EventRepository(private val service: EventApiService) : RemoteDataSource() {

    suspend fun getEvents(): Result<List<Event>> = request(service.getEventListAsync())

    suspend fun getEventsByStatus(status: String): Result<List<Event>> = request(service.getEventListByStatusAsync(status))

    suspend fun getEventInfo(eventId: String): Result<EventInfo> = request(service.getEventInfoAsync(eventId))

    suspend fun getProductsInEvents(eventId: String): Result<List<Product>> = request(service.getProductListInEventAsync(eventId))
}