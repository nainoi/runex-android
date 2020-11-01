package com.think.runex.feature.event

import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.event.model.Event
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class EventViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun getAllEvent(): List<Event>? = withContext(IO) {
        val result = repo.getAllEvent()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }
}