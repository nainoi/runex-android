package com.think.runex.feature.event

import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.event.model.registered.RegisteredEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun getRegisteredEvents(): List<RegisteredEvent>? = withContext(Dispatchers.IO) {
        val result = repo.getRegisteredEvents()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }
}