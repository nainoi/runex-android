package com.think.runex.feature.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.think.runex.datasource.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepo: EventRepository) : BaseViewModel() {

    val events: MutableLiveData<List<Event>> by lazy { MutableLiveData<List<Event>>() }

    fun getEvents(status: String? = null) {
        viewModelScope.launch(IO) {
            val result = if (status.isNullOrBlank()) eventRepo.getEvents() else eventRepo.getEventsByStatus(status)
            when (result.isSuccessful()) {
                true -> postEvensData(result.data)
                false -> onHandleError(result.statusCode, result.message)
            }
        }
    }

    private fun postEvensData(events: List<Event>?) = viewModelScope.launch(Dispatchers.Main) {
        this@EventViewModel.events.value = events
    }

    suspend fun getEventInfo(eventId: String): Event? {
        var event: Event? = null
        viewModelScope.launch(IO) {
            val result = eventRepo.getEventInfo(eventId)
            when (result.isSuccessful()) {
                true -> event = result.data?.event
                false -> onHandleError(result.statusCode, result.message)
            }
        }.join()
        return event
    }
}