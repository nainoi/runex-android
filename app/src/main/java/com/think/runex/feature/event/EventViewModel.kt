package com.think.runex.feature.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jozzee.android.core.utility.Logger
import com.think.runex.common.toJson
import com.think.runex.datasource.BaseViewModel
import com.think.runex.java.Constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.ArrayList

class EventViewModel(private val eventRepo: EventRepository) : BaseViewModel() {

    val allEvents: MutableLiveData<List<Event>> by lazy { MutableLiveData<List<Event>>() }

    fun getAllEvents(status: String? = null) {
        viewModelScope.launch(IO) {
            val result = eventRepo.getAllEvents(status)
            when (result.isSuccessful()) {
                true -> postEvensData(result.data)
                false -> onHandleError(result.statusCode, result.message)
            }
        }
    }

    private fun postEvensData(events: List<Event>?) = viewModelScope.launch(Main) {
        this@EventViewModel.allEvents.value = events
    }
}