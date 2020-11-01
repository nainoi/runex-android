package com.think.runex.feature.event

import com.think.runex.datasource.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class EventViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun isRegisteredEvent(eventId: String): Boolean = withContext(IO) {
        val result = repo.isRegisteredEvent(eventId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data?.isRegistered ?: false
    }
}